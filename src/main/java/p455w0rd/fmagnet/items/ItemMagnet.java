package p455w0rd.fmagnet.items;

import javafx.scene.control.TextFormatter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class ItemMagnet extends Item {

    private static final String NBT_MAGNET_MODE = "MagnetMode";

    public ItemMagnet() {
        super(new Settings().maxCount(1).group(ItemGroup.TOOLS));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack magnet = player.getStackInHand(hand);
        if (!world.isClient && player.isSneaking()) {
            cycleMode(magnet);
            return new TypedActionResult<>(ActionResult.SUCCESS, magnet);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, magnet);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        list_1.add(new TranslatableText("tooltip.fmagnet.mode." + getMagnetMode(itemStack_1)));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean hasGlint(ItemStack magnet) {
        return isActive(magnet);
    }

    public boolean isActive(ItemStack magnet) {
        return getMagnetMode(magnet).getBoolean();
    }

    private void setMagnetMode(ItemStack magnet, MagnetMode mode) {
        checkTag(magnet);
        magnet.getTag().putBoolean(NBT_MAGNET_MODE, mode.getBoolean());
    }

    private MagnetMode getMagnetMode(ItemStack magnet) {
        if (!magnet.isEmpty()) {
            checkTag(magnet);
            return magnet.getTag().getBoolean(NBT_MAGNET_MODE) ? MagnetMode.ACTIVE : MagnetMode.INACTIVE;
        }
        return MagnetMode.INACTIVE;
    }

    private void cycleMode(ItemStack magnet) {
        MagnetMode currentMode = getMagnetMode(magnet);
        if (currentMode.getBoolean()) {
            setMagnetMode(magnet, MagnetMode.INACTIVE);
            return;
        }
        setMagnetMode(magnet, MagnetMode.ACTIVE);
    }

    private void checkTag(ItemStack magnet) {
        if (!magnet.isEmpty()) {
            if (!magnet.hasTag()) {
                magnet.setTag(new CompoundTag());
            }
            CompoundTag nbt = magnet.getTag();
            if (!nbt.contains(NBT_MAGNET_MODE)) {
                nbt.putBoolean(NBT_MAGNET_MODE, false);
            }
        }
    }

    public enum MagnetMode {

        ACTIVE(true), INACTIVE(false);

        final boolean state;

        MagnetMode(boolean state) {
            this.state = state;
        }

        public boolean getBoolean() {
            return state;
        }

    }

}
