--AVATAR BY RELEAYED

--[[-------------------------------------------------------------
-----------------------------------------------------------------
                           CREDITS
                   Auria - patpat, Ear Physics
                       Xander - justlean
                       Shiji - ShiSkirt
                   ChloeSpacedOut - PhysBoneAPI
                     JimmyHelp - EZAnims
                    CircleManiac - WobbleLIB

-----------------------------------------------------------------]]

vanilla_model.PLAYER:setVisible(false)
vanilla_model.ARMOR:setVisible(false)
vanilla_model.HELMET_ITEM:setVisible(true)
vanilla_model.CAPE:setVisible(false)
vanilla_model.ELYTRA:setVisible(false)

local patpat = require("patpat")
local cAPI = require("just-lean")
local wobblelib = require('CMwubLib')
local earsPhysics = require('ears')

local bodyWobble = wobblelib:newWobbleSetup()
isCrouching = false

local ears = earsPhysics.new(models.model.root.Torso.Head.Hair.Blep, models.model.root.Torso.Head.Hair.OtherHiddenBlep)

nameplate.ALL:setText('[{"text":"Kasane Frickin Teto ${badges}", "bold":false, "color":"#c7425a"}]')
nameplate.ENTITY:setOutline(true):setOutlineColor(61 / 255, 12 / 255, 21 / 255):setBackgroundColor(0, 0, 0, 0)


function events.entity_init() 
  models.model:setScale(0.95, 0.95, 0.95)
  renderer:setShadowRadius(0.4)
  models.model.root.Torso.Head.HelmetItemPivot:setScale(0.8, 0.8, 0.8)
end

function events.WORLD_RENDER()
    if player:isLoaded() then
        bodyWobble:update(player:getVelocity().y,true)
        models.model.root:setScale(1 + bodyWobble.wobble*0.2,1 - bodyWobble.wobble*0.4,1 + bodyWobble.wobble*0.2)
        if player:isCrouching() and isCrouching == false then
            bodyWobble:setWobble(0.1,0.1,0.1)
            isCrouching = true
        elseif not player:isCrouching() and isCrouching == true then
            bodyWobble:setWobble(-0.1,-0.1,-0.1)
            isCrouching = false
        end
        
    end
end

function events.render(delta, context)
  if player:isLoaded() then
    if player:isCrouching() then
      models.model.root.Torso:setPos(0, 1.4, -0.5)
      models.model.root.Torso.Body.skirt:setPos(0, 0, 0.1)
      models.model.root:setPos(0,2,0)
    else
      models.model.root.Torso:setPos(0, 0, 0)
      models.model.root:setPos(0,0,0)
      models.model.root.Torso.Body.skirt:setPos(0, 0, 0)
    end
    
    headrot = ((player:getRot(delta).y - player:getBodyYaw(delta) + 180) % 360 - 180) / 90
    models.model.root.Torso.Head.Eyes.Irises.LeftIris:setPos(math.clamp(headrot, -0.4, 0.1), 0, 0)
    models.model.root.Torso.Head.Eyes.Irises.RightIris:setPos(math.clamp(headrot, -0.1, 0.4), 0, 0)
  end
end


num1 = ((252/255) - (156/255))/2
num2 = ((136/255) - (59/255))/2
num3 = ((179/255) - (77/255))/2

other1 = num1 + (156/255)
other2 = num2 + (59/255)
other3 = num3 + (77/255)

ringfloat = 0
colorTime = 0
ringspin = 0
function events.tick()
  colorTime = colorTime + 0.05
  

  nameColor = vec(
    other1 + math.sin(colorTime) * num1,
    other2 + math.sin(colorTime) * num2,
    other3 + math.sin(colorTime) * num3
  )
  nameplate.ENTITY:setText('[{"text":"Kasane frickin Teto ${badges}\n", "bold":true, "color":"'.."#"..tostring(vectors.rgbToHex(nameColor))..'"}]')
end



local torso = cAPI.lean:new(models.model.root.Torso,
  { x = -20, y = -15 }, { x = 20, y = 15 }, 
  0.5, "inOutCubic", true, true)

local leftarm = cAPI.influence:new(models.model.root.Torso.LeftArm,
    0.3, "inOutSine",
    { 1, 0.2, 0.2 }, nil, true, true)
local rightarm = cAPI.influence:new(models.model.root.Torso.RightArm,
    0.3, "inOutSine",
    { 1, 0.2, 0.2 }, nil, true, true)
local leftleg = cAPI.influence:new(models.model.root.LeftLeg,
    0.5,"linear",
    {0, -0.5, -1}, torso, true, true)
local rightleg = cAPI.influence:new(models.model.root.RightLeg,
    0.5, "linear",
    {0, -0.5, -1}, torso, true, true)

local mainPage = action_wheel:newPage()
action_wheel:setPage(mainPage)
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
function pings.dance(state)
  isDancing = state
  if state then
    animations.model.mesmerized:play()
  else
    animations.model.mesmerized:stop()
  end
end
local danceTime = mainPage:newAction()
  :title("dance"):item("nether_star")
  :setOnToggle(pings.dance)