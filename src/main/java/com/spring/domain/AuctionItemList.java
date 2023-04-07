package com.spring.domain;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class AuctionItemList {
    private final Map<Long, Item> auctionItemList = new HashMap<>();

    public boolean add(Long itemId, Item item) {
        auctionItemList.put(itemId, item);
        return true;
    }

    public Item getItemById(Long itemId) {
        
    	return auctionItemList.get(itemId);
    }

    public HashSet<Item> getItems() {
        return new HashSet<>(auctionItemList.values());
    }
    
    public void remove(Long itemId) {
    	auctionItemList.remove(itemId);
    }

}
