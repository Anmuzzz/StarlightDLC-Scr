--=====#region MODEL PART SETTINGS =====

vanilla_model.PLAYER:setVisible(false)
vanilla_model.ARMOR:setVisible(false)
vanilla_model.ELYTRA:setVisible(false)


models.model.root.torso.body.necklace.necklace2.nl_boing.shine1:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.root.torso.body.necklace.necklace2.nl_boing.shine2:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.root.torso.head.earrings.rightearring.shine_e2:setSecondaryRenderType("EMISSIVE_SOLID")
models.model.root.torso.head.earrings.leftearring.shine_e1:setSecondaryRenderType("EMISSIVE_SOLID")

--=====#endregion 
--=====#region ANIMATION SETTINGS =====

require("libs.GSAnimBlend")
local anims = require("libs.EZAnims")
local howl = anims:addBBModel(animations.model)

animations.model.idle_arms:play()

animations.model.walkingback:setBlendTime(5)

--=====#endregion 
--=====#region TEXTURE ANIMATION =====

--== necklace sparkle

local ns_frames = {
	vec(0,0),
	vec(10/30,0),
	vec(10/30,0),
	vec(0,10/30),
	vec(-10/30,0),
	vec(-10/30,0),
	vec(20/30,10/30),
	vec(-10/30,0),
	vec(-10/30,0)
}

function events.tick()
	models.model.root.torso.body.necklace.necklace2.nl_boing.sparkleanim:setVisible(animations.model.attackR:isPlaying() or animations.model.mineR:isPlaying())

	local time = world.getTime() 
	if animations.model.attackR:isPlaying() or animations.model.mineR:isPlaying() then
	models.model.root.torso.body.necklace.necklace2.nl_boing.sparkleanim:setUV(ns_frames[(time % #ns_frames) + 1])
end
end

--=====#endregion
--=====#region BASIC BODY PHYSICS =====

local squapi = require("libs.SquAPI")

--== lean walk

      local prevVel = vec(0,0,0)
      local curVel = vec(0,0,0)
    function events.tick()
        prevVel = curVel
        curVel = player:getVelocity()
    end

      local walkStr = 20
    function events.render(delta)
          
      local vel = math.lerp(prevVel,curVel,delta):transform(matrices.rotation3(0, player:getRot().y)) * walkStr
      local moveRot = vec(-vel.z*0.7, 0, vel.x*0.7)

        models.model.root:offsetRot(moveRot/1.1)
        models.model.root.torso:offsetRot(-moveRot/4)
    end

local b_rot = 0;
function events.render(d)
    if not player:getVehicle() then
        b_rot = math.lerp(b_rot, vanilla_model.LEFT_LEG:getOriginRot().x / 8, d / 6)
        models.model.root.torso.body:setRot(0, b_rot, b_rot / 8)
        models.model.root.torso.body.necklace.necklace2.nl_boing:setRot(0, b_rot, -b_rot)
    end
end

--== smoove

	local _rot
	local rot = { 0,0,0 }

	local head_part = models.model.root.torso.head
	local torso_part = models.model.root.torso
	local cloak_part = models.model.root.torso.body.cloak.cloakrot

	function events.tick()
	  _rot = (vanilla_model.HEAD:getOriginRot() + 180) % 360 - 180
	end
	function GetRotations()
	  rot[1] = math.lerp(rot[1], _rot.x,0.1)

	  rot[2] = math.lerp(rot[2], _rot.y,0.1)
	end
	function SmoothBodyRot(modelpart, xmod, ymod, zxmod, zymod)
	  zxmod = zxmod or 0
	  zymod = zymod or 0

	  modelpart:setRot(rot[1] * xmod, rot[2] * ymod, rot[1] * zxmod + rot[2] * zymod)
	  modelpart:setPos(-rot[1] * xmod / 200, -rot[2] * ymod / 2000, rot[1] * zxmod / 200 + rot[2] * zymod / 200)
	end

function events.render()
  GetRotations()

  -- smoove body
    SmoothBodyRot(head_part, 0.7,0.7,0)
    SmoothBodyRot(torso_part, 0.3, 0.3, 0)
end

--== eyes

models.model.root.torso.head.eyes.eye_L_light:setSecondaryRenderType("EMISSIVE")
models.model.root.torso.head.eyes.eye_R_light:setSecondaryRenderType("EMISSIVE_SOLID")

function events.render()
    headRotX = (vanilla_model.HEAD:getOriginRot().x+180)%360-180
    headRotY = (vanilla_model.HEAD:getOriginRot().y+180)%360-180

  if headRotY > 0 then
    --eyeliner
      --models.model.root.torso.head.eyes.eyeliner:setPos(0,math.clamp(headRotX / 100 * .8, -.8, 1),0)
    --eyes
      models.model.root.torso.head.eyes.eye_L:setPos(math.clamp(headRotY / 75 * -1, -1, 0),math.clamp(headRotX / 75 * 1, -.6, 1),0)
      models.model.root.torso.head.eyes.eye_R:setPos(math.clamp(headRotY / 75 * -2, -1, 0),math.clamp(headRotX / 75 * 1, -.6, 1),0)
    --highlights
      models.model.root.torso.head.eyes.eye_L_light:setPos(math.clamp(headRotY / 100 * 1, 1, 0),math.clamp(headRotX / 150 * 2, -1, .2),0)
      models.model.root.torso.head.eyes.eye_R_light:setPos(math.clamp(headRotY / 100 * 1, 1, 0),math.clamp(headRotX / 150 * 2, -1, .2),0)

  else if headRotY < 0 then
    --eyeliner
      --models.model.root.torso.head.eyes.eyeliner:setPos(0,math.clamp(headRotX / 100 * .8, -.8, 1),0)
    --eyes
      models.model.root.torso.head.eyes.eye_L:setPos(math.clamp(headRotY / 75 * -2, 0, 1),math.clamp(headRotX / 75 * 1, -.6, 1),0)
      models.model.root.torso.head.eyes.eye_R:setPos(math.clamp(headRotY / 75 * -1, 0, 1),math.clamp(headRotX / 75 * 1, -.6, 1),0)
    --highlights
      models.model.root.torso.head.eyes.eye_L_light:setPos(math.clamp(headRotY / 100 * -1, -1, 1),math.clamp(headRotX / 150 * 2, -1, .2),0)
      models.model.root.torso.head.eyes.eye_R_light:setPos(math.clamp(headRotY / 100 * -1, -1, 1),math.clamp(headRotX / 150 * 2, -1, .2),0)

  end
  end
end

--== limbs

local rot_r, rot_l
local is_flying
function events.tick(delta, context)
	isFlying = player:getPose() == "FALL_FLYING"
end

-- squ arms

	squapi.arm:new(models.model.root.torso.arms.rightarm, 0.4, true, false) 
	squapi.arm:new(models.model.root.torso.arms.leftarm, 0.4, isRight, false) 

function events.render(delta, context)
    rot_r = vanilla_model.RIGHT_LEG:getOriginRot()
    rot_l = vanilla_model.LEFT_LEG:getOriginRot()
    

        local firstPerson = context == "FIRST_PERSON"
    models.model.rightarm_fp:setVisible(firstPerson):setParentType(context=="FIRST_PERSON" and "RightArm" or "None")
    models.model.leftarm_fp:setVisible(firstPerson):setParentType(context=="FIRST_PERSON" and "LeftArm" or "None")

	if not isFlying then
    -- arms 
	    models.model.root.torso.arms.rightarm:setRot(-rot_l * 0.4)
	    models.model.root.torso.arms.leftarm:setRot(-rot_r * 0.4)
    -- legs
	    models.model.root.legs.rightleg:setRot(-rot_r * 0.5)
	    models.model.root.legs.leftleg:setRot(-rot_l * 0.5)
	end

	-- legs & boing updown
    	local isWalking = player:getVelocity().xz:length() ~= 0
    if player:isSprinting() or isWalking then
        models.model.root:setPos(0, math.abs(vanilla_model.LEFT_LEG:getOriginRot()[1]/150), 0)
    end
    if isWalking then
        models.model.root.legs.leftleg:setPos(0, math.abs(vanilla_model.RIGHT_LEG:getOriginRot()[1]/50), 0)
        models.model.root.legs.rightleg:setPos(0, math.abs(vanilla_model.RIGHT_LEG:getOriginRot()[1]/50), 0)
    end
end

--=====#endregion
--=====#region SWINGING PHYSICS =====

local SwingingPhysics = require("libs.swinging_physics")

--[[swingOnBody(part, dir, limits, root, depth)
    dir = Returns movement angle relative to look direction (2D top down view, ignores Y)
        0   : forward
        45  : left forward
        90  : left
        135 : left backwards
        180 : backwards
        -135: right backwards
        -90 : right
        -45 : right forward
     limits (min, max): {x, x, y, y, z, z}]]

--== hair

SwingingPhysics.swingOnHead(models.model.root.torso.head.hair.fronthair, 0, { -2, 5, -10, 0, -5, 5 }, nil, 0)
SwingingPhysics.swingOnHead(models.model.root.torso.head.hair.fronthair.fronthair_L, 0, { -2, 7, -5, 0, -7, 7 }, nil, 0)
SwingingPhysics.swingOnHead(models.model.root.torso.head.hair.fronthair.fronthair_R, 0, { -2, 7, -5, 0, -7, 7 }, nil, 0)

SwingingPhysics.swingOnHead(models.model.root.torso.head.hair.righthair, -90, { -2, 7, -10, 0, -7, 7 }, nil, 0)
SwingingPhysics.swingOnHead(models.model.root.torso.head.hair.lefthair, 90, { -2, 7, -10, 0, -7, 7 }, nil, 0)
SwingingPhysics.swingOnHead(models.model.root.torso.head.hair.backhair, 180, { -2, 7, -10, 0, -7, 7 }, nil, 0)

--== jacket

SwingingPhysics.swingOnBody(models.model.root.torso.body.cloak, 180, {-50,2,-15,15,0,0})
SwingingPhysics.swingOnBody(models.model.root.torso.body.cloak.cloakrot.midrightcloak, -135, {0,0,-5,5,-2,2})
SwingingPhysics.swingOnBody(models.model.root.torso.body.cloak.cloakrot.midleftcloak, 135, {0,0,-5,5,-2,2})

SwingingPhysics.swingOnBody(models.model.root.torso.body.cloak.cloakrot.leftcloak, 135, {-10,20,0,0,-20,20})
SwingingPhysics.swingOnBody(models.model.root.torso.body.cloak.cloakrot.leftcloak.leftcloak2, 90, {-40,5,0,0,-20,20})

SwingingPhysics.swingOnBody(models.model.root.torso.body.cloak.cloakrot.rightcloak, -135, {-10,20,0,0,-20,20})
SwingingPhysics.swingOnBody(models.model.root.torso.body.cloak.cloakrot.rightcloak.rightcloak2, -90, {-40,5,0,0,-20,20})

--== necklace

SwingingPhysics.swingOnBody(models.model.root.torso.body.necklace, 0, {-2,20,-15,15,-10,10})
SwingingPhysics.swingOnBody(models.model.root.torso.body.necklace.necklace2, 0, {-2,20,-15,15,-15,15})

--== earrings

SwingingPhysics.swingOnHead(models.model.root.torso.head.earrings.leftearring, 0, {-30,30,-30,30,-30,30})
SwingingPhysics.swingOnHead(models.model.root.torso.head.earrings.rightearring, 0, {-30,30,-30,30,-30,30})

--=====#endregion
--=====#region CONFIG ACTION WHEEL =====

--== wheel setup

local mainPage = action_wheel:newPage()
action_wheel:setPage(mainPage)

--== jacket

local jacketVis = config:load("JVis") or 0
function events.tick()
    if jacketVis == 0 then
        models.model.root.torso.body.cloak:setVisible(true)
    else
        models.model.root.torso.body.cloak:setVisible(false)
    end
end
function pings.changeJVis()
    jacketVis = (jacketVis + 1) % 2
    config:save("DVis", jacketVis)
    sounds:playSound("minecraft:item.armor.equip_leather", player:getPos(), 1, 1, false)
end
local jacketVisTbl = {
    {
        title = "Jacket: On",
        item = ("golden_chestplate")
    },
    {
        title = "Jacket: Off",
        item = ("barrier")
    }
}
local jacketVisChange = mainPage:newAction()
    :onToggle(pings.changeJVis)

function events.TICK()
    jacketVisChange
        :title(jacketVisTbl[jacketVis+1].title)
        :item(jacketVisTbl[jacketVis+1].item)
end

--== hair colour

local hairCol = config:load("HCol") or 0
function events.tick()
    if hairCol == 0 then
        colour = vectors.hexToRGB("#FFE493")  
    end
    if hairCol == 1 then
        colour = vectors.hexToRGB("#444453")
    end
    if hairCol == 2 then
        colour = vectors.hexToRGB("#ffae4d")
    end
    models.model.root.torso.head.hair:setColor(colour)
    models.model.Skull.hair2:setColor(colour)
end
function pings.changeHCol()
    hairCol = (hairCol + 1) % 3
    config:save("HCol", hairCol)
    sounds:playSound("minecraft:item.armor.equip_leather", player:getPos(), 1, 1, false)
    sounds:playSound("minecraft:block.amethyst_block.step", player:getPos(), 1, 2, false)
end
local hairColTbl = {
    {
        title = "Hair: Blonde",
        item = ("golden_helmet")
    },
    {
        title = "Hair: Black",
        item = ("netherite_helmet")
    },
    {
        title = "Hair: Orange",
        item = ("leather_helmet")
    }
}
local hairColChange = mainPage:newAction()
    :onToggle(pings.changeHCol)

function events.TICK()
    hairColChange
        :title(hairColTbl[hairCol+1].title)
        :item(hairColTbl[hairCol+1].item)
end

--== sync toggle configs 

function pings.syncVariables(a, b)
    jacketVis = a
    hairCol = b
end
function events.tick()
    if world.getTime() % 200 == 0 then
        pings.syncVariables(jacketVis, hairCol)
    end
end

--=====#endregion
--=====#region BLINK =====

function events.tick()
    local randomtime = math.random(-300,300)

    if world.getTime() % randomtime == 0 then
        animations.model.blink:play()
    end
end

--=====#endregion