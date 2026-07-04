vanilla_model.PLAYER:setVisible(false)
vanilla_model.CHESTPLATE:setVisible(false)
vanilla_model.HELMET:setVisible(false)
vanilla_model.LEGGINGS:setVisible(false)
vanilla_model.BOOTS:setVisible(false)

--========== SKIN

models.model.whole.root.torso.Head.headrot.vanillahead:setPrimaryTexture("SKIN")
models.model.whole.root.torso.Body.vanillabody:setPrimaryTexture("SKIN")
models.model.whole.root.torso.arms.LeftArm:setPrimaryTexture("SKIN")
models.model.whole.root.torso.arms.RightArm:setPrimaryTexture("SKIN")
models.model.whole.LeftLeg:setPrimaryTexture("SKIN")
models.model.whole.RightLeg:setPrimaryTexture("SKIN")

--========== API SETTINGS

require("GSAnimBlend")
local squapi = require("SquAPI")
local anims = require("JimmyAnims")
anims(animations.model)
local extras = require("JimmyExtras")
extras.landvel = -1
extras.jimmyCompat = true
extras.raintimer = 60
extras.drytimer = 60
extras.excluBlendTime = 4
extras.incluBlendTime = 10
extras.autoBlend = true
extras.dismiss = false
extras.addExcluAnimsController()
extras.addIncluAnimsController()
extras.addAllAnimsController()
extras(animations.model)

local SwingingPhysics = require("swinging_physics")
local swingOnHead = SwingingPhysics.swingOnHead
local swingOnBody = SwingingPhysics.swingOnBody

--========== SQU
    -- random [blink] animations
        squapi.blink(animations.model.earflick)
        squapi.blink(animations.model.earflickR)
        squapi.blink(animations.model.earflickL)

--========== AURIA

	-- little tail
		local tailPhy = require('tail')
		local Tail = tailPhy.new(models.model.whole.root.torso.Body.tail)

	-- wag when v is pressed
		keybinds:newKeybind("feelers - wag", "key.keyboard.v")
		   :onPress(function() pings.tailWag(true) end)
		   :onRelease(function() pings.tailWag(false) end)
		function pings.tailWag(v)
		   Tail.config.enableWag.keybind = v
		end

	-- wag during walk
		keybinds:newKeybind("feelers - wag", "key.keyboard.w")
		   :onPress(function() pings.tailWag(true) end)
		   :onRelease(function() pings.tailWag(false) end)
		function pings.tailWag(w)
		   Tail.config.enableWag.keybind = w
		end
		keybinds:newKeybind("feelers - wag", "key.keyboard.a")
		   :onPress(function() pings.tailWag(true) end)
		   :onRelease(function() pings.tailWag(false) end)
		function pings.tailWag(a)
		   Tail.config.enableWag.keybind = a
		end
		keybinds:newKeybind("feelers - wag", "key.keyboard.s")
		   :onPress(function() pings.tailWag(true) end)
		   :onRelease(function() pings.tailWag(false) end)
		function pings.tailWag(s)
		   Tail.config.enableWag.keybind = s
		end
		keybinds:newKeybind("feelers - wag", "key.keyboard.d")
		   :onPress(function() pings.tailWag(true) end)
		   :onRelease(function() pings.tailWag(false) end)
		function pings.tailWag(d)
		   Tail.config.enableWag.keybind = d
		end


--========== GSAnimBlend // ANIMATIONS & SETTINGS

animations.model.defaultstate:play()
animations.model.notparticle:play()

--========== PRIORITY

  animations.model.isparticle:setPriority(1)

--========== BLENDTIME

-- idles
  animations.model.tap_idle:setBlendTime(3)
  animations.model.shifty_idle:setBlendTime(3)
  animations.model.armswing_idle:setBlendTime(3)

-- emotes
  animations.model.dab:setBlendTime(3)
  animations.model.sits:setBlendTime(3)
  animations.model.wave:setBlendTime(3)
  animations.model.bounce:setBlendTime(3)
  animations.model.laugh:setBlendTime(5)
  animations.model.spin:setBlendTime(0)
  animations.model.excited:setBlendTime(3)
  animations.model.sadness:setBlendTime(3)
  animations.model.bow:setBlendTime(3)
  animations.model.glasses:setBlendTime(0)
  animations.model.glassesback:setBlendTime(0)
    animations.model.glassesback:setSpeed(-1)

-- movement
  animations.model.sprintjumpup:setBlendTime(2)
  animations.model.fly:setBlendTime(6)
  animations.model.flywalk:setBlendTime(6)
  animations.model.flywalkback:setBlendTime(6)
  animations.model.flyup:setBlendTime(6)
  animations.model.flydown:setBlendTime(6)

  animations.model.land:setBlendTime(2)
  animations.model.fall:setBlendTime(2)

  animations.model.attackR:setBlendTime(1)
  animations.model.attackL:setBlendTime(1)
  animations.model.blockR:setBlendTime(1)
  animations.model.blockL:setBlendTime(1)
  animations.model.mineR:setBlendTime(1)
  animations.model.mineL:setBlendTime(1)
  animations.model.useR:setBlendTime(1)
  animations.model.useL:setBlendTime(1)
  animations.model.bowR:setBlendTime(1)
  animations.model.bowL:setBlendTime(1)
  animations.model.crossbowR:setBlendTime(1)
  animations.model.crossbowL:setBlendTime(1)
  animations.model.blockR:setBlendTime(1)
  animations.model.blockL:setBlendTime(1)
  animations.model.loadR:setBlendTime(1)
  animations.model.loadL:setBlendTime(1)
  animations.model.spyglassR:setBlendTime(1)
  animations.model.spyglassL:setBlendTime(1)
  animations.model.holdR:setBlendTime(1)
  animations.model.holdL:setBlendTime(1)
  animations.model.brushR:setBlendTime(1)
  animations.model.eatR:setBlendTime(1)
  animations.model.eatL:setBlendTime(1)
  animations.model.spearR:setBlendTime(1)

--========== SPEED
-- emotes
  --animations.model.spin:setSpeed(1.3)

-- movement
function events.tick()
  animations.model.walk:setSpeed(0.9+player:getVelocity():length()*1.5)
  animations.model.waterup:setSpeed(0.7)
  animations.model.waterdown:setSpeed(0.7)
  animations.model.waterwalk:setSpeed(0.7)
  animations.model.waterwalkback:setSpeed(-0.7)
  --animations.model.jumpup:setSpeed(0.65)
  animations.model.walkback:setSpeed(-0.9-player:getVelocity():length()*1.5)
  animations.model.crouchwalk:setSpeed(1.4)
  animations.model.crouchwalkback:setSpeed(-1.2)
  animations.model.sprint:setSpeed(0.4+player:getVelocity():length()*1.2)
  animations.model.sprintjumpup:setSpeed(0.5)
end

--========== ACTION WHEEL SETUP

local page = action_wheel:newPage()
action_wheel:setPage(page)

local Emotes = action_wheel:newPage('Emotes')
    page:newAction(1):title('Emotes'):item("minecraft:pufferfish"):onLeftClick(function() action_wheel:setPage(Emotes) end)
    Emotes:newAction(1):title('Back'):item("minecraft:arrow"):onLeftClick(function() action_wheel:setPage(page) end) 

--========== MAIN WHEEL

-- == dab ==
    function pings.dab() 
    	animations.model.dab:play()
	end 
Emotes:newAction()
    :title("Dab")
    :hoverColor(1,0,0.3)
    :item("minecraft:stick")
    :onLeftClick(pings.dab)

-- == laugh ==
    function pings.laugh() 
        animations.model.laugh:play()
    end 
Emotes:newAction()
    :title("Laugh")
    :hoverColor(1,0,0.3)
    :item("minecraft:stick")
    :onLeftClick(pings.laugh)

-- == wave ==
    function pings.wave() 
        animations.model.wave:play()
    end 
Emotes:newAction()
    :title("Wave")
    :hoverColor(1,0,0.3)
    :item("minecraft:stick")
    :onLeftClick(pings.wave)

-- == sits ==
    function pings.sits(state)
        if state then
            animations.model.sits:play()
        else
            animations.model.sits:stop()
            animations.model.hurt:play()
        end
    end
local sitting = page:newAction()
    :title("Sits")
    :toggleTitle("Get Up")
    :hoverColor(0, 1, 1)
    :item("minecraft:pumpkin")
    :onToggle(pings.sits)

-- == spin ==
    function pings.spin() 
        animations.model.spin:play()
    end 
Emotes:newAction()
    :title("Spin!")
    :hoverColor(1,0,0.3)
    :item("minecraft:stick")
    :onLeftClick(pings.spin)

-- == excited ==
    function pings.excited() 
        animations.model.excited:play()
    end 
Emotes:newAction()
    :title("Excited taps")
    :hoverColor(1,0,0.3)
    :item("minecraft:stick")
    :onLeftClick(pings.excited)

-- == bow ==
    function pings.bow() 
        animations.model.bow:play()
    end 
Emotes:newAction()
    :title("Bow")
    :hoverColor(1,0,0.3)
    :item("minecraft:stick")
    :onLeftClick(pings.bow)

-- == sadness ==
    function pings.sadness(state)
        if state then
            animations.model.sadness:play()
            animations.model.hurt:play()
        else
            animations.model.sadness:stop()
            animations.model.hurt:play()
        end
    end
page:newAction()
    :title("Sadness")
    :toggleTitle("No Longer Sad")
    :hoverColor(1,0,0.3)
    :item("minecraft:coal")
    :onToggle(pings.sadness)

-- == glasses ==
    function pings.glasses(state)
        if state then
            animations.model.glassesback:play()
            animations.model.glasses:stop()
        else
            animations.model.glassesback:stop()
            animations.model.glasses:play()
        end
    end
page:newAction()
    :title("Put on Sunglasses")
    :toggleTitle("Take off Sunglasses")
    :hoverColor(1,0,0.3)
    :item("minecraft:glass_pane")
    :onToggle(pings.glasses)

-- == happy bounce ==
    function pings.bounce(state)
        if state then
            animations.model.bounce:play()
        else
            animations.model.bounce:stop()
            animations.model.earflick:play()
            animations.model.hurt:play()
        end
    end
page:newAction()
    :title("Happy Bounce")
    :toggleTitle("Stop Bouncing")
    :hoverColor(1,0,0.3)
    :item("minecraft:honey_block")
    :onToggle(pings.bounce)

-- == idles off ==
    function pings.idlesoff(state)
        if state then
            animations.model.idlesoff:play()
        else
            animations.model.idlesoff:stop()
        end
    end
page:newAction()
    :title("Idle Animations On")
    :toggleTitle("Idle Animations Off")
    :hoverColor(1,0,0.3)
    :item("minecraft:spore_blossom")
    :toggleItem("minecraft:barrier")
    :onToggle(pings.idlesoff)

--========== EVENTS // PARTICLES
       --== particles:newParticle(particleID, position, velocity)
             --== sounds:playSound(soundID, position, volume, pitch, loop)

function events.tick()
  time = world.getTime()

  -- if particle // when moving
  local notMoving = animations.model.idle:isPlaying() or animations.model.crouch:isPlaying()
  local offsetx = math.random(-100,100) * math.random() / 6
  local offsetz = math.random(-50,50) * math.random() / 6
  local randomtime = math.random(-25,25)
  local randomtimeLong = math.random(-800,800)

    if not notMoving and world.getTime() % randomtime == 0 then
      animations.model.isparticle:play()
      particles:newParticle("minecraft:firework", models.model.whole.root.torso.Body.particle:partToWorldMatrix(10,10,10):apply(offsetx,0,offsetz)):setScale(0.4):setLifetime(20)
    end

  -- sits
  local isSitting = animations.model.sits:isPlaying()
  local isMoving = animations.model.walk:isPlaying() or animations.model.walkback:isPlaying() or animations.model.crouch:isPlaying() or animations.model.jumpup:isPlaying()
    if isMoving and isSitting then 
        sitting:setToggled(false) 
        animations.model.hurt:play()
        animations.model.sits:stop()
    end

  -- spin sparkles emote
    if animations.model.spin:isPlaying() and world.getTime() % randomtime == 0 then
        particles:newParticle("minecraft:end_rod", models.model.whole.root.torso.arms.LeftArm.LeftItemPivot:partToWorldMatrix(10,10,10):apply()):setScale(0.4):setLifetime(10)
        particles:newParticle("minecraft:end_rod", models.model.whole.root.torso.arms.RightArm.RightItemPivot:partToWorldMatrix(10,10,10):apply()):setScale(0.4):setLifetime(10)
    end

  -- glasses
    if animations.model.glassesback:isPlaying() or animations.model.glasses:isPlaying() then 
        models.model.whole.root.torso.Head.headrot.glasses:setVisible(true)
    end

  -- random starhorn sparks
  local starhornBits = {
    models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle1.leftbit1,
    models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2.leftbit2,
    models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle3.leftbit3,
    models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2.leftbit2.leftdangle4.leftbit4,

    models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle1.rightbit1,
    models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2.rightbit2,
    models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle3.rightbit3,
    models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2.rightbit2.rightdangle4.rightbit4,
  }
    if world.getTime() % randomtimeLong == 0 then
        for _, Bit in ipairs(starhornBits) do
            if world.getTime() % randomtimeLong == 0 then
                particles:newParticle("minecraft:end_rod", starhornBits[math.random(#starhornBits)]:partToWorldMatrix():apply()):setScale(0.4):setLifetime(30)
                break
            end
        end
    end

end

--========== IDLES
local wasIdle = false
local isIdle = false
local idling = 0
local idles = {
    animations.model.tap_idle,
    animations.model.shifty_idle,
    animations.model.armswing_idle,
}
local emotes = {
    animations.model.dab,
    animations.model.glasses,
    animations.model.glassesback,
    animations.model.sits,
    animations.model.wave,
    animations.model.bounce,
    animations.model.laugh,
    animations.model.spin,
    animations.model.excited,
    animations.model.sadness,
    animations.model.bow,
}
local emotePlaying = false

function events.tick()
    wasIdle = isIdle
    local idlesOff = animations.model.idlesoff:isPlaying()
    local crouching = player:getPose() == "CROUCHING"
    local sprinting = player:isSprinting()
    local walking = player:getVelocity().xz:length() > 0.01 or animations.model.walk:isPlaying()
    local jumping = animations.model.jumpup:isPlaying()

    -- Check if any emote is playing
    emotePlaying = false
    for _, emote in ipairs(emotes) do
        if emote:isPlaying() then
            emotePlaying = true
            break
        end
    end

    if not (walking or crouching or idlesOff or jumping or emotePlaying) then
        idling = idling + 1
    else
        idling = 0
    end

    if idling > 200 then
        isIdle = true
    else
        isIdle = false
    end

    if not wasIdle and isIdle and not emotePlaying then
        for _, idle in ipairs(idles) do
            idle:stop()
        end
        idles[math.random(#idles)]:play()
    end

    -- Stop idle animations if an emote starts playing
    if emotePlaying then --and isIdle
        for _, idle in ipairs(idles) do
            idle:stop()
        end
        isIdle = false
        idling = 0
    end
end

--========== PHYSICS
-- swingOnBody(part, dir, limits, root, depth)
    -- dir = Returns movement angle relative to look direction (2D top down view, ignores Y)
        -- 0   : forward
        -- 45  : left forward
        -- 90  : left
        -- 135 : left backwards
        -- 180 : backwards
        -- -135: right backwards
        -- -90 : right
        -- -45 : right forward
    -- limits (min, max): {x, x, y, y, z, z}

swingOnBody(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle1.rightbit1, -180, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2.rightbit2, -180, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle3.rightbit3, -135, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2.rightbit2.rightdangle4.rightbit4, -180, {-10,10,-10,10,-10,10})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle3.rightbit3up, -135, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle1.leftbit1, 180, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2.leftbit2, 180, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle3.leftbit3, 135, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2.leftbit2.leftdangle4.leftbit4, 180, {-10,10,-10,10,-10,10})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle3.leftbit3up, 135, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle1.rightbit1up, 180, {-70,70,-70,70,-70,70})
swingOnBody(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle1.leftbit1up, 180, {-70,70,-70,70,-70,70})

swingOnHead(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle1, 180, {-90,50,-90,50,-90,50})
swingOnHead(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2, 180, {-90,50,-90,50,-90,50})
swingOnHead(models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle3, 135, {-90,50,-90,50,-90,50})
swingOnHead(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle1, 180, {-90,50,-90,50,-90,50})
swingOnHead(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2, 180, {-90,50,-90,50,-90,50})
swingOnHead(models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle3, -135, {-90,50,-90,50,-90,50})


--========== TEXTURE
-- models._:setSecondaryRenderType("CUTOUT, CUTOUT_CULL, TRANSLUCENT, TRANSLUCENT_CULL, EMISSIVE, EMISSIVE_SOLID, EYES, END_PORTAL, 
                                -- END_GATEWAY, TEXTURED_PORTAL, GLINT, GLINT2, TEXTURED_GLINT, LINES, LINES_STRIP, SOLID, BLURRY")

models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle1.rightbit1:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle1.rightbit1up:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2.rightbit2:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle3.rightbit3:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2.rightbit2.rightdangle4:setSecondaryRenderType("EMISSIVE")
models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle2.rightbit2.rightdangle4.rightbit4:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.righthorn.rightdangle3.rightbit3up:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle1.leftbit1:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle1.leftbit1up:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2.leftbit2:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle3.leftbit3:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2.leftbit2.leftdangle4:setSecondaryRenderType("EMISSIVE")
models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle2.leftbit2.leftdangle4.leftbit4:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.whole.root.torso.Head.headrot.horns.lefthorn.leftdangle3.leftbit3up:setSecondaryRenderType("EMISSIVE_SOLID")








