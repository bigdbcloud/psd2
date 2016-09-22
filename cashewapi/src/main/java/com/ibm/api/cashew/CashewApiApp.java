package com.ibm.api.cashew;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ibm.api.cashew.services.UserAccountService;
import com.ibm.api.cashew.services.barclays.BarclaysService;

@SpringBootApplication
public class CashewApiApp implements CommandLineRunner
{
	@Autowired
	private BarclaysService barclayService;
	
	@Autowired
	private UserAccountService userAcctService;
	
	public static void main(String[] args)
	{
		SpringApplication.run(CashewApiApp.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
	
		
		List<com.ibm.api.cashew.beans.Transaction> list=userAcctService.getTransactions("1369505709744231","BARCGB","6122", null, null, null, null, 0, 0);
		System.out.println(list);//barclayService.getTransactions("8573315966758940");
	}
}
