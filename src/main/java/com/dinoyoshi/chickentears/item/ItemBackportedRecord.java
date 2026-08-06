package com.dinoyoshi.chickentears.item;

import com.dinoyoshi.chickentears.ChickenTears;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemBackportedRecord extends ItemRecord {
    public ItemBackportedRecord(String registryId, String recordName, SoundEvent sound) {
        super(recordName, sound);
        setRegistryName(ChickenTears.MODID, registryId);
        setUnlocalizedName("record");
    }
}
