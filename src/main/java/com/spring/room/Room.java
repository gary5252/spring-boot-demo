package com.spring.room;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.spring.domain.Item;

public class Room {

    private final String owner;
    private final ArrayList<String> guests;
    private final String roomId;
    private Item item;
    
    public Room(String roomId,String ownerName) {
        this.roomId = roomId;
        this.owner = ownerName;
        this.guests = new ArrayList<>();
    }

	public String getOwner() {
		return owner;
	}

	public ArrayList<String> getGuests() {
		return guests;
	}

	public String getRoomId() {
		return roomId;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
    public boolean addGuest(String guestName) {
//        if (this.guests.size() >= 3) {
//            return false;
//        }
        this.guests.add(guestName);
        return true;
    }

    public void removeGuest(String guestName) {
        this.guests.remove(guestName);
    }

    public int count() {
        return 1 + this.guests.size();
    }

    public ArrayList<String> getAllMembers() {
        ArrayList<String> allPeople = new ArrayList<>();
        allPeople.add(this.owner);
        allPeople.addAll(this.guests);
        return allPeople;
    }

    public Map<String, Object> getInfo() {
        Map<String , Object> info = new HashMap<>();
        info.put("roomId", this.roomId);
        info.put("owner", this.owner);
        info.put("guests", this.guests);
        info.put("number", this.count());
        info.put("item", this.item);
        return info;
    }
    
}
