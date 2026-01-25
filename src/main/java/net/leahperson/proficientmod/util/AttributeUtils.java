package net.leahperson.proficientmod.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.concurrent.atomic.AtomicBoolean;

public class AttributeUtils {
    public static Multimap<Attribute, AttributeModifier> combineAttributes(Multimap<Attribute, AttributeModifier> mapa, Multimap<Attribute, AttributeModifier> mapb){

        Multimap<Attribute, AttributeModifier> mapc = HashMultimap.create();
        //create a list C
        //Take any matches from A and B and combine and put them into C
        //Take any non matches from A and any non matches from B and put them into C


        mapa.forEach((keya,elema)->{
            mapb.forEach((keyb,elemb)-> {
                if (keya.equals(keyb) && elema.getOperation().equals(elemb.getOperation())) {
                    if (elema.getOperation().equals(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
                        mapc.put(keya, new AttributeModifier(elema.getId(), elema.getName(), elema.getAmount() * elemb.getAmount(), elema.getOperation()));

                    } else {
                        mapc.put(keya, new AttributeModifier(elema.getId(), elema.getName(), elema.getAmount() + elemb.getAmount(), elema.getOperation()));

                    }

                }
            });
        });
        mapa.forEach((keya,elema)-> {
            AtomicBoolean found = new AtomicBoolean(false);
            mapb.forEach((keyb, elemb) -> {
                if ((keya.equals(keyb) && elema.getOperation().equals(elemb.getOperation()))) {
                    found.set(true);
                }
            });
            if(!found.get()){
                mapc.put(keya,elema);
            }
        });
        mapb.forEach((keyb,elemb)-> {
            AtomicBoolean found = new AtomicBoolean(false);
            mapa.forEach((keya, elema) -> {
                if ((keya.equals(keyb) && elema.getOperation().equals(elemb.getOperation()))) {
                    found.set(true);
                }
            });
            if(!found.get()){
                mapc.put(keyb,elemb);
            }
        });
        return mapc;
    }
}
