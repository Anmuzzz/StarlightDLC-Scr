package com.isusdlc.systems.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import com.isusdlc.systems.setting.SettingsContainer;
import com.isusdlc.systems.setting.impl.AbstractSetting;
import org.jetbrains.annotations.NotNull;

public class StringSetting extends AbstractSetting {
   private String text;

   public StringSetting(@NotNull SettingsContainer parent, String name, String description, @NotNull BooleanSupplier hideCondition) {
      super(parent, name, hideCondition);
   }

   public StringSetting(@NotNull SettingsContainer parent, String name, @NotNull BooleanSupplier hideCondition) {
      super(parent, name, hideCondition);
   }

   public StringSetting(@NotNull SettingsContainer parent, String name, String description) {
      super(parent, name);
   }

   public StringSetting(@NotNull SettingsContainer parent, String name) {
      super(parent, name);
   }

   public StringSetting text(String text) {
      this.text = text;
      return this;
   }

   @Override
   public JsonElement save() {
      return new JsonPrimitive(this.text);
   }

   @Override
   public void load(JsonElement element) {
      this.text(element.getAsString());
   }

   @Generated
   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      if (!Objects.equals(this.text, text)) {
         this.text = text;
      }
   }
}
