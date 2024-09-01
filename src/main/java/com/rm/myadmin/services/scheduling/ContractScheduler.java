package com.rm.myadmin.services.scheduling;

import java.time.LocalDate;
import java.time.Year;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rm.myadmin.entities.Contract;
import com.rm.myadmin.entities.RentalHistory;
import com.rm.myadmin.entities.enums.ContractStatus;
import com.rm.myadmin.entities.enums.PaymentStatus;
import com.rm.myadmin.services.ContractService;
import com.rm.myadmin.services.RentalHistoryService;

@Component
public class ContractScheduler {
	@Autowired
	private ContractService contractService;

	@Autowired
	private RentalHistoryService rentalHistoryService;

	@Scheduled(cron = "0 50 20 * * *")
	public void checkContracts() {
		LocalDate today = LocalDate.now();
		Set<Contract> contracts = contractService.findByContractStatus(1);

		for (Contract c : contracts) {
			try {
				if (c.getContractEndDate().equals(today)) {
					c.setContractStatus(ContractStatus.TERMINATED);
				} else {
					int dueDate = c.getInvoiceDueDate();
					if ((int) dueDate > today.getMonth().length(Year.isLeap(today.getYear()))) {
						dueDate = today.getMonth().length(Year.isLeap(today.getYear()));
					}

					if (dueDate == today.getDayOfMonth()) {
						System.out.println("O contrato com ID " + c.getId() + " está vencido hoje.");
						RentalHistory rental = new RentalHistory(null, today, PaymentStatus.PENDING, c);
						rentalHistoryService.create(rental);
						contractService.sendInvoiceByEmail(c, rental);
					}
				}
			} catch (Exception e) {
				System.err.println("Erro ao processar contrato com ID " + c.getId() + ": " + e.getMessage());
			}
		}
		System.out.println("verificações do dia finalizadas.");
	}
}
