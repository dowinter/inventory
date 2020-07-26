package de.dowinter.rest;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import de.dowinter.model.InventoryItem;
import de.dowinter.rest.representations.AvailabilityResult;
import de.dowinter.rest.representations.ErrorResult;
import de.dowinter.service.InventoryService;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/inventory/items")
public class InventoryResource {

    @Inject
    InventoryService inventoryService;

    @GET
    @Path("/{itemId}/availability")
    @Produces(MediaType.APPLICATION_JSON)
    public AvailabilityResult getAvailablity(@PathParam("itemId") long itemId,
                                             @QueryParam("count") int count) {
        Optional<InventoryItem> item = inventoryService.getItem(itemId);

        boolean isAvailable = item.map(inventoryItem -> inventoryService.checkAvailability(inventoryItem, count))
                .orElse(false);

        return new AvailabilityResult(isAvailable);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<InventoryItem> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSingleItem(@PathParam("itemId") long itemId) {
        Optional<InventoryItem> item = inventoryService.getItem(itemId);
        if (item.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok(item.get()).build();
    }

    @POST
    @Path("/{itemId}/decrease")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(Transactional.TxType.REQUIRED)
    public Response removeFromInventory(@PathParam("itemId") long itemId, @QueryParam("count") int count) {
        Optional<InventoryItem> item = inventoryService.getItem(itemId);

        if (item.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        try {
            inventoryService.removeStock(item.get(), count);
        } catch (IllegalStateException exception) {
            return Response.status(BAD_REQUEST).entity(new ErrorResult(exception.getMessage())).build();
        }

        return Response.ok().build();
    }
}
