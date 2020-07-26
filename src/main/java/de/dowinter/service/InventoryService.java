/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dowinter.service;

import de.dowinter.data.InventoryRepository;
import de.dowinter.model.InventoryItem;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class InventoryService {
    @Inject
    InventoryRepository inventory;

    public void addItem(InventoryItem item) {
        inventory.save(item);
    }

    public boolean checkAvailability(InventoryItem inventoryItem, int count) {
        return Optional.ofNullable(inventoryItem)
                .map(item -> item.getInStockCount() >= count).orElse(false);
    }

    public void removeStock(InventoryItem item, int count) {
        int newCount = item.getInStockCount() - count;

        if (newCount < 0) {
            throw new IllegalStateException("Stock should not fall below 0.");
        }

        item.setInStockCount(newCount);
    }

    public List<InventoryItem> getAllItems() {
        return inventory.findAllOrderedByName();
    }

    public Optional<InventoryItem> getItem(long itemId) {
        return Optional.ofNullable(inventory.findById(itemId));
    }
}
