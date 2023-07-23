package com.cognixia.jump.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.model.Contact;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.ContactRepository;

@Component
public class ContactService {

	@Autowired
	private ContactRepository repo;
	
	public List<Contact> getContacts(User user) {
		List<Contact> list = repo.findByUserId(user.getId().intValue());
		if (list.isEmpty()) {
			return new ArrayList<Contact>();
		}
		return list;
	}
	
	public Contact newContact(Contact contact) {
		contact.setId(null);
		contact.setLastAccessed(LocalDateTime.now());
		
		return repo.save(contact);
	}
	
	public void sortContacts(User user, String sortMethod) {
		if (sortMethod.equals("first")) {
			Collections.sort(user.getContact(), new Comparator<Contact>() {
				@Override
				public int compare(Contact e1, Contact e2) {
					return e1.getFirstName().compareTo(e2.getFirstName());
				}
			});
		} else if (sortMethod.equals("last")) {
			Collections.sort(user.getContact(), new Comparator<Contact>() {
				@Override
				public int compare(Contact e1, Contact e2) {
					return e1.getLastName().compareTo(e2.getLastName());
				}
			});
		} else if (sortMethod.equals("recent")) {
			Collections.sort(user.getContact(), new Comparator<Contact>() {
				@Override
				public int compare(Contact e1, Contact e2) {
					return e1.getLastAccessed().compareTo(e2.getLastAccessed());
				}
			});
		} else if (sortMethod.equals("email")) {
			Collections.sort(user.getContact(), new Comparator<Contact>() {
				@Override
				public int compare(Contact e1, Contact e2) {
					return e1.getEmail().compareTo(e2.getEmail());
				}
			});
		} else {
			Collections.sort(user.getContact(), new Comparator<Contact>() {
				@Override
				public int compare(Contact e1, Contact e2) {
					return e1.getId().compareTo(e2.getId());
				}
			});
		}
		
	}

	public void updateContact(Contact update) {
		update.setLastAccessed(LocalDateTime.now());
		repo.save(update);
	}
	
	public void deleteContact(Contact delete) {
		repo.deleteById(delete.getId());
	}
	
}
