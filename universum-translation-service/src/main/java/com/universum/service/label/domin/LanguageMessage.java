package com.universum.service.label.domin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class LanguageMessage implements Serializable {
	private static final long serialVersionUID = 8132554332381825302L;
	@ElementCollection
	private List<Message> messages;
	
	public void addMessage(final Message message) {
		if(this.messages == null) this.messages = new ArrayList<>();
		this.messages.add(message);
	}
}
