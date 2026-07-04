package com.isusdlc.utility.render.batching.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.framework.msdf.MsdfFont;
import com.isusdlc.framework.msdf.MsdfRenderer;
import com.isusdlc.utility.render.batching.Batching;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.gl.ShaderProgram;

public class FadeOutBatching extends Batching {
   protected MsdfFont font;
   private float fadeoutStart;
   private float fadeoutEnd;
   private float maxWidth;
   private float x;

   public FadeOutBatching(VertexFormat vertexFormat, MsdfFont font, float fadeoutStart, float fadeoutEnd, float maxWidth, float x) {
      super(vertexFormat);
      this.font = font;
      this.fadeoutStart = fadeoutStart;
      this.fadeoutEnd = fadeoutEnd;
      this.maxWidth = maxWidth;
      this.x = x;
   }

   @Override
   public void draw() {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, this.font.getTextureId());
      ShaderProgram shader = RenderSystem.setShader(MsdfRenderer.MSDF_FONT_SHADER_KEY);
      float thickness = 0.05F;
      float smoothness = 0.5F;
      shader.getUniform("Range").set(this.font.getAtlas().range());
      shader.getUniform("Thickness").set(thickness);
      shader.getUniform("Smoothness").set(smoothness);
      shader.getUniform("EnableFadeout").set(1);
      shader.getUniform("FadeoutStart").set(this.fadeoutStart);
      shader.getUniform("FadeoutEnd").set(this.fadeoutEnd);
      shader.getUniform("MaxWidth").set(this.maxWidth);
      shader.getUniform("TextPosX").set(this.x);
      this.build();
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      if (active == this) {
         active = null;
      }
   }
}
