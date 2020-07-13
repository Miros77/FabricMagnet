package p455w0rd.fmagnet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import p455w0rd.fmagnet.init.ModItems;
import p455w0rd.fmagnet.items.ItemMagnet;

import java.util.List;

public class FabricMagnet implements ModInitializer {

    public static final FabricMagnet INSTANCE = new FabricMagnet();

    @Override
    public void onInitialize() {
        ModItems.register();
        ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> {
            FabricMagnet.INSTANCE.doMagnet(minecraftServer);
        });
    }

    public void doMagnet(MinecraftServer server) {
        List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : playerList) {
            PlayerInventory inv = player.inventory;
            for (int i = 0; i < inv.size(); i++) {
                if (inv.getStack(i).getItem() == ModItems.MAGNET) {
                    ItemStack magnetStack = inv.getStack(i);
                    ItemMagnet magnet = (ItemMagnet) magnetStack.getItem();
                    if (magnet.isActive(magnetStack)) {
                        List<ItemEntity> entityItems = player.getServerWorld().getEntities(ItemEntity.class, player.getBoundingBox().expand(16.0D), EntityPredicates.VALID_ENTITY);
                        for (ItemEntity entityItemNearby : entityItems) {
                            if (!player.isSneaking()) {
                                entityItemNearby.onPlayerCollision(player);
                            }
                        }
                    }
                }
            }
        }
    }

}
