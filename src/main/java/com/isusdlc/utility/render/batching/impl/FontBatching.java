package com.isusdlc.utility.render.batching.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.framework.msdf.MsdfFont;
import com.isusdlc.framework.msdf.MsdfRenderer;
import com.isusdlc.utility.render.batching.Batching;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.gl.ShaderProgram;

public class FontBatching extends Batching {
   protected MsdfFont font;

   public FontBatching(VertexFormat vertexFormat, MsdfFont font) {
      super(vertexFormat);
      this.font = font;
   }

   @Override
   public void draw() {
      float thickness = 0.05F;
      float smoothness = 0.5F;
      float spacing = 0.0F;
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, this.font.getTextureId());
      ShaderProgram shader = RenderSystem.setShader(MsdfRenderer.MSDF_FONT_SHADER_KEY);
      shader.getUniform("Range").set(this.font.getAtlas().range());
      shader.getUniform("Thickness").set(thickness);
      shader.getUniform("Smoothness").set(smoothness);
      shader.getUniform("EnableFadeout").set(0);
      this.build();
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      if (active == this) {
         active = null;
      }
   }
}
