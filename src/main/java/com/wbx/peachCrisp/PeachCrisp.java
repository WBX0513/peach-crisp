package com.wbx.peachCrisp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeachCrisp implements ModInitializer {

    public static final String MOD_ID = "peach-crisp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item PEACH_CRISP = new Item(new Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "peach_crisp")))
        .food(
            new FoodProperties(4, 0.6f, false),
            Consumable.builder()
                .consumeSeconds(1.6f)
                .animation(ItemUseAnimation.EAT)
                .sound(SoundEvents.GENERIC_EAT)
                .hasConsumeParticles(true)
                .onConsume(new ApplyStatusEffectsConsumeEffect(
                    new MobEffectInstance(MobEffects.REGENERATION, 60, 1), 1.0f
                ))
                .build()
        )
    );

    public static void initialize() {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "peach_crisp"), PEACH_CRISP);
    }

    @Override
    public void onInitialize() {
        initialize();

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(
            creativeTab -> creativeTab.accept(PEACH_CRISP)
        );

        ResourceLoader.registerBuiltinPack(
            Identifier.fromNamespaceAndPath(MOD_ID, "balanced_diet_ext"),
            FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
            PackActivationType.ALWAYS_ENABLED
        );

        LOGGER.info("Peach Crisp mod initialized!");
    }
}
