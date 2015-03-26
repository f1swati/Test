package org.gradle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
@RequestMapping("/api/v1")
public class MyVoteController {

	private Map<Integer, Moderator> moderatorMap;
	private Map<String, Poll> pollMap;
	private AtomicLong along;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public MyVoteController() {
		moderatorMap = new HashMap<Integer, Moderator>();
		pollMap=new HashMap<String, Poll>();
		along = new AtomicLong(123456);
	}

	@RequestMapping(value = "/moderators", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Moderator> createModerator( @Valid@RequestBody Moderator moderator) {

		moderator.setId((int) along.incrementAndGet());
		moderator.setCreated_at(Calendar.getInstance());
		moderatorMap.put(moderator.getId(), moderator);
		return new ResponseEntity<Moderator>(moderator,HttpStatus.CREATED);
	}

	@RequestMapping(value = "/moderators/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Moderator> getModerator(@PathVariable int id) {
			return new ResponseEntity<Moderator>(moderatorMap.get(id),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/moderators/{id}", method = RequestMethod.PUT, produces=MediaType.APPLICATION_JSON)
	@ResponseBody
	public ResponseEntity<Object> updateModerator( @PathVariable int id,@Validated(Views.Moderator.class) @RequestBody Moderator moderator,Errors errors) {
		if(errors.hasErrors()) {return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST); }
		Moderator moderatorActual=moderatorMap.get(id);
		moderatorActual.setEmail(moderator.getEmail());
		moderatorActual.setPassword(moderator.getPassword());
		return new ResponseEntity<Object>(moderatorMap.get(id),HttpStatus.OK) ;
	}
	
	@RequestMapping(value = "/moderators/{id}/polls", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON)
	@ResponseBody
	public ResponseEntity<Object> createPoll(@Valid@RequestBody Poll poll,@NotBlank@PathVariable int id) {
		
			poll.setId(null);
			pollMap.put(poll.getId(), poll);
			List<String> pollIds= moderatorMap.get(id).getPollIds();
			pollIds.add(poll.getId());
			return new ResponseEntity<Object>(poll,HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/polls/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getPoll(@NotBlank @PathVariable String id,HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectWriter objectWriter = objectMapper.writerWithView(Views.User.class);
			return new ResponseEntity<String>(objectWriter.writeValueAsString(pollMap.get(id)),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/moderators/{moderator_id}/polls/{pollId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getModeratorPoll(@NotBlank @PathVariable int moderator_id,@NotBlank @PathVariable String pollId){
			return new ResponseEntity<Object>(pollMap.get(pollId),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/moderators/{moderator_id}/polls", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getModeratorPolls(@NotBlank @PathVariable int moderator_id){
			List<String> pollIdList=moderatorMap.get(moderator_id).getPollIds();
			List<Poll> polls=new ArrayList<Poll>();
			for(String poolId:pollIdList){
				polls.add(pollMap.get(poolId));
			}
			return new ResponseEntity<Object>(polls,HttpStatus.OK);
	}

	@RequestMapping(value = "/moderators/{moderator_id}/polls/{pollId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deletePoll(@NotBlank @PathVariable int moderator_id,@NotBlank @PathVariable String pollId){
			moderatorMap.get(moderator_id).getPollIds().remove(pollId);
			pollMap.remove(pollId);
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		
	}
	
	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Object> castVote(@NotBlank @PathVariable String pollId,@NotBlank@RequestParam int choice ){
			Poll poll = pollMap.get(pollId);
			List<Integer> results = poll.getResults();
			Integer count=results.get(choice);
			results.remove(choice);
			results.add(choice, ++count);
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
			
	}
}
