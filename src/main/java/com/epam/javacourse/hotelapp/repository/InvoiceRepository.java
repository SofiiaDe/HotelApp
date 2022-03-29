package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("SELECT i FROM Invoice i WHERE i.userId.id = :id")
    List<Invoice> findInvoicesByUserId(@Param("id") int userId);

    @Query("SELECT i FROM Invoice i WHERE i.bookingId.id IN :ids")
    List<Invoice> findInvoicesByBookingsIds(@Param("ids") List<Integer> bookingsIds);

    @Modifying
    @Query("UPDATE Invoice i SET i.invoiceStatus = :status where i.id = :id")
    void updateInvoiceStatus(@Param("status") String status, @Param("id") Integer invoiceId);

    @Query("SELECT i FROM Invoice i WHERE i.invoiceStatus = :status")
    List<Invoice> findInvoicesByStatus(@Param("status") String status);
}
