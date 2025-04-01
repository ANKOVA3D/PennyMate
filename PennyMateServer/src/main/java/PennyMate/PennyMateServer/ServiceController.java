package PennyMate.PennyMateServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transactions")
public class ServiceController {
	
	@Autowired
    private TransactionRepos Repos;

}
