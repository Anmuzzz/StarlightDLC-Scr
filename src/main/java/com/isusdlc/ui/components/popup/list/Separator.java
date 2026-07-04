package com.isusdlc.ui.components.popup.list;

import com.isusdlc.framework.base.UIContext;
import com.isusdlc.ui.components.popup.PopupComponent;
import com.isusdlc.utility.colors.Colors;

public class Separator extends PopupComponent {
   @Override
   protected void renderComponent(UIContext context) {
      context.drawRect(this.x, this.y, this.width, this.height, Colors.getSeparatorColor());
   }

   @Override
   public float getHeight() {
      return this.height = 4.0F;
   }
}
