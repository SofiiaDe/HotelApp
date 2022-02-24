package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.model.Invoice;

public class InvoiceMapper implements DtoMapper<InvoiceDto, Invoice>{

@Override
public InvoiceDto mapToDto(Invoice invoice){
        InvoiceDto dto = new InvoiceDto();
        dto.setId(invoice.getId());
        dto.setUserId(invoice.getUserId().getId());
        dto.setAmount(invoice.getAmount());
        dto.setBookingId(invoice.getBookingId().getId());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setStatus(invoice.getInvoiceStatus());
        return dto;
        }

@Override
public Invoice mapFromDto(InvoiceDto dto){
        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setUserId(dto.getUser());
        invoice.setAmount(dto.getAmount());
        invoice.setBookingId(dto.getBooking());
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setInvoiceStatus(dto.getStatus());
        return invoice;
        }
}
