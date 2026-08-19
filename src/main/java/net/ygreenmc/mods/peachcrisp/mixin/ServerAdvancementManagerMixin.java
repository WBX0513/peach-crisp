package net.ygreenmc.mods.peachcrisp.mixin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.world.item.Item;

import net.ygreenmc.mods.peachcrisp.PeachCrisp;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {

    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @ModifyVariable(
        method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private Map<Identifier, Advancement> peachcrisp$appendBalancedDietFoods(Map<Identifier, Advancement> advancements) {
        Identifier key = Identifier.fromNamespaceAndPath("minecraft", "husbandry/balanced_diet");
        Advancement original = advancements.get(key);
        if (original == null) {
            return advancements;
        }

        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

        Map<String, Criterion<?>> criteria = new LinkedHashMap<>(original.criteria());
        criteria.put("peach_crisp", ConsumeItemTrigger.TriggerInstance.usedItem(items, PeachCrisp.PEACH_CRISP));
        criteria.put("golden_peach_crisp", ConsumeItemTrigger.TriggerInstance.usedItem(items, PeachCrisp.GOLDEN_PEACH_CRISP));

        Advancement modified = new Advancement(
            original.parent(),
            original.display(),
            original.rewards(),
            criteria,
            AdvancementRequirements.allOf(criteria.keySet()),
            original.sendsTelemetryEvent(),
            original.name()
        );

        Map<Identifier, Advancement> result = new HashMap<>(advancements);
        result.put(key, modified);
        return result;
    }
}
