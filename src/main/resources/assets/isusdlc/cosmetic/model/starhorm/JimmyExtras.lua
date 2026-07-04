-- + Made by Jimmy Hellp
-- + V1 for 0.1.0 and above
-- + Thank you GrandpaScout for helping with the library stuff!
-- + Automatically compatible with GSAnimBlend for automatic smooth animation blending
-- + Also includes Manuel's Run Later script

------------------------------------

local animsList = {
    -- Exclusive Animations
    land = "landing from a great height",
    waterland = "landing in water from a great height",
    inrain = "standing in the rain for a bit",
    dry = "getting out of the rain",
    -- Inclusive Animations
    fireworkR = "using a firework in right hand while elytra gliding",
    fireworkL = "using a firework in left hand while elytra gliding",
    firebowR = "firing a bow from the right hand",
    firebowRL = "firing a bow from the left hand",
    firecrossbowR = "firing a crossbow from the right hand",
    firecrossbowL = "firing a crossbow from the left hand",
    blockedR = "shield in right hand blocked an attack",
    blockedL = "shield in left hand blocked an attack",
}

------------------------------------------------------------------------------------------------------------------------

local function errors(paths,dismiss)
    assert(
        next(paths),
        "§aCustom Script Warning: §6No blockbench models were found, or the blockbench model found contained no animations. \n" .." Check that there are no typos in the given bbmodel name, or that the bbmodel has animations by using this line of code at the top of your script: \n"
        .."§f logTable(animations.BBMODEL_NAME_HERE) \n ".."§6If this returns nil your bbmodel name is wrong or it has no animations. You can use \n".."§f logTable(models:getChildren()) \n".."§6 to get the name of every bbmodel in your avatar.§c"
    )

    for _, path in pairs(paths) do
        for _, anim in pairs(path) do
            if anim:getName():find("%.") and not dismiss then
                error(
                    "§aCustom Script Warning: §6The animation §b'"..anim:getName().."'§6 has a period ( . ) in its name, the handler can't use that animation and it must be renamed to fit the handler's accepted animation names. \n" ..
                " If the animation isn't meant for the handler, you can dismiss this error by adding §fextras.dismiss = true§6 after the require but before setting the bbmodel.§c")
            end
        end
    end
end

local bbmodels = {} -- don't put things in here

-- wait code from Manuel
local timers = {}

local function wait(ticks,next)
    table.insert(timers,{t=world.getTime()+ticks,n=next})
end

events.TICK:register(function()
    for key,timer in pairs(timers) do
        if world.getTime() >= timer.t then
            timer.n()
            timers[key] = nil
        end
    end
end)

local allAnims = {}
local excluAnims = {}
local incluAnims = {}
local animsTable= {
    allVar = false,
    excluVar = false,
    incluVar = false
}
local excluState
local incluState

local bowR
local oldbowR
local bowL
local oldbowL

local crossR
local oldcrossR
local crossL
local oldcrossL

local oldvel
local vel

local oldrain
local rain
local raintimer = 0
local drytimer = 61
local drying = false

local leftswing
local rightswing
local usingR
local usingL

local rightActive
local leftActive
local activeness

local landvel = -1
local setrain = 60
local setdry = 60

function events.entity_init()
    vel = player:getVelocity().y
    oldvel = vel
    
    rain = player:isWet()
    oldrain = rain
end

local function tick()
    for _, value in ipairs(allAnims) do
        if value:isPlaying() then
            animsTable.allVar = true
            break
        else
            animsTable.allVar = false
        end
    end
    for _, value in ipairs(excluAnims) do
        if value:isPlaying() then
            animsTable.excluVar = true
            break
        else
            animsTable.excluVar = false
        end
    end

    for _, value in ipairs(incluAnims) do
        if value:isPlaying() then
            animsTable.incluVar = true
            break
        else
            animsTable.incluVar = false
        end
    end

    excluState = not animsTable.allVar and not animsTable.excluVar
    incluState = not animsTable.allVar and not animsTable.incluVar

    local water = player:isInWater()
    local handedness = player:isLeftHanded()
    rightActive = handedness and "OFF_HAND" or "MAIN_HAND"
    leftActive = not handedness and "OFF_HAND" or "MAIN_HAND"
    activeness = player:getActiveHand()
    local using = player:isUsingItem()
    
    local rightItem = player:getHeldItem(handedness)
    local leftItem = player:getHeldItem(not handedness)
    usingR = activeness == rightActive and rightItem:getUseAction()
    usingL = activeness == leftActive and leftItem:getUseAction()
    local swingarm = player:getSwingArm()

    leftswing = swingarm == leftActive

    rightswing = swingarm == rightActive
    
    oldcrossR = crossR
    crossR = rightItem.tag and rightItem.tag["Charged"] == 1
    oldcrossL = crossL
    crossL = leftItem.tag and leftItem.tag["Charged"] == 1

    oldbowR = bowR
    bowR = using and usingR == "BOW"
    oldbowL = bowL
    bowL = using and usingL == "BOW"

    -- land
    oldvel = vel
    vel = player:getVelocity().y
    if vel > landvel and oldvel < landvel and player:isOnGround() then
        if not water then
            for _, path in pairs(bbmodels) do if path.land and excluState then path.land:play() end end
        else
            for _, path in pairs(bbmodels) do if path.waterland and excluState then path.waterland:play() end end
        end
    end

    -- rain
    oldrain = rain
    rain = player:isWet()
    if rain and not water then
        raintimer = raintimer + 1
        drytimer = 0
        drying = false
    elseif not rain then
        drying = true
    elseif water then
    else
        raintimer = 0
        drytimer = 0
    end

    if drying and drytimer < 61 then
        drytimer = drytimer + 1
        raintimer = 0
    end

    if raintimer == setrain then
        for _, path in pairs(bbmodels) do if path.inrain and excluState then path.inrain:play() end end
    elseif drytimer == setdry then
        for _, path in pairs(bbmodels) do if path.dry and excluState then path.dry:play() end end
    end
end

function events.on_play_sound(id, pos)
    if not player:isLoaded() then return end
    if id == "minecraft:entity.arrow.shoot" then
        wait(1,function()
            if oldbowR ~= bowR and oldbowR then for _, path in pairs(bbmodels) do if path.firebowR and incluState then path.firebowR:play() end end end
            if oldbowL ~= bowL and oldbowL then for _, path in pairs(bbmodels) do if path.firebowL and incluState then path.firebowL:play() end end end
        end)
    elseif id == "minecraft:item.crossbow.shoot" then
        wait(1,function()
            if oldcrossR ~= crossR and oldcrossR then for _, path in pairs(bbmodels) do if path.firecrossbowR and incluState then path.firecrossbowR:play() end end end
            if oldcrossL ~= crossL and oldcrossL then for _, path in pairs(bbmodels) do if path.firecrossbowL and incluState then path.firecrossbowL:play() end end end
        end)
    elseif id == "minecraft:item.shield.block" and (pos-player:getPos()):length() < 1 then
        if activeness == rightActive and usingR == "BLOCK" then for _, path in pairs(bbmodels) do if path.blockedR and incluState then path.blockedR:play() end end end
        if activeness == leftActive  and usingL == "BLOCK" then for _, path in pairs(bbmodels) do if path.blockedL and incluState then path.blockedL:play() end end end
    elseif id == "minecraft:entity.firework_rocket.launch" and player:isGliding() then
        wait(1,function()
            if leftswing then for _, path in pairs(bbmodels) do if path.fireworkL and incluState then path.fireworkL:play() end end end
            if rightswing then for _, path in pairs(bbmodels) do if path.fireworkR and incluState then path.fireworkR:play() end end end
        end)
    end
end

local JimmyAnims
for _, key in ipairs(listFiles(nil,true)) do
    if key:find("JimmyAnims$") then
        JimmyAnims = require(key)
        break
    end
end

local function compat(paths)
    if not JimmyAnims then return end
    for _, path in pairs(paths) do
        if path.inrain then JimmyAnims.addExcluAnimsController(path.inrain) end
        if path.dry then JimmyAnims.addExcluAnimsController(path.dry) end
        if path.land then JimmyAnims.addExcluAnimsController(path.land) end
        if path.waterland then JimmyAnims.addExcluAnimsController(path.waterland) end

        if path.fireworkR then JimmyAnims.addIncluAnimsController(path.fireworkR) end
        if path.fireworkL then JimmyAnims.addIncluAnimsController(path.fireworkL) end
        if path.firebowR then JimmyAnims.addIncluAnimsController(path.firebowR) end
        if path.firebowL then JimmyAnims.addIncluAnimsController(path.firebowL) end
        if path.firecrossbowR then JimmyAnims.addIncluAnimsController(path.firecrossbowR) end
        if path.firecrossbowL then JimmyAnims.addIncluAnimsController(path.firecrossbowL) end
        if path.blockedR then JimmyAnims.addIncluAnimsController(path.blockedR) end
        if path.blockedL then JimmyAnims.addIncluAnimsController(path.blockedL) end
    end
end

local GSAnimBlend
for _, key in ipairs(listFiles(nil,true)) do
    if key:find("GSAnimBlend$") then
        GSAnimBlend = require(key)
        break
    end
end
if GSAnimBlend then GSAnimBlend.safe = false end

local function blend(paths, time, itemTime)
    if not GSAnimBlend then return end
    for _, path in pairs(paths) do
        if path.inrain then path.inrain:blendTime(time) end
        if path.dry then path.dry:blendTime(time) end
        if path.land then path.land:blendTime(time) end
        if path.waterland then path.waterland:blendTime(time) end

        if path.fireworkR then path.fireworkR:blendTime(itemTime) end
        if path.fireworkL then path.fireworkL:blendTime(itemTime) end
        if path.firebowR then path.firebowR:blendTime(itemTime) end
        if path.firebowL then path.firebowL:blendTime(itemTime) end
        if path.firecrossbowR then path.firecrossbowR:blendTime(itemTime) end
        if path.firecrossbowL then path.firecrossbowL:blendTime(itemTime) end
        if path.blockedR then path.blockedR:blendTime(itemTime) end
        if path.blockedL then path.blockedL:blendTime(itemTime) end
    end
end

wait(20,function()
    assert(
     next(bbmodels),
    "§aCustom Script Warning: §6JimmyExtrass isn't being required, or a blockbench model isn't being provided to it. \n".."§6 Put this code in a DIFFERENT script to use JimmyExtras: \n".."§flocal extras = require('JimmyExtras') \n"..
    "§fextras(animations.BBMODEL_NAME_HERE) \n".."§6 Where you replace BBMODEL_NAME_HERE with the name of your bbmodel. \n".."§6 Or go to the top of the script or to the top of the Discord forum for more complete instructions.".."§c") 
 end)

local init = false
local animMT = {__call = function(self, ...)
    local paths = {...}
    local should_blend = true
    local should_compat = true
    if self.autoBlend ~= nil then should_blend = self.autoBlend end
    if self.jimmyCompat ~= nil then should_compat = self.jimmyCompat end
    if self.landvel ~= nil then landvel = self.landvel end
    if self.raintimer ~= nil then setrain = self.raintimer end
    if self.drytimer ~= nil then setdry = self.drytimer end

    errors(paths,self.dismiss)

    for _, v in ipairs(paths) do
        bbmodels[#bbmodels+1] = v
    end

    -- Init stuff.
    if init then return end
    if should_blend then blend(paths, self.excluBlendTime or 4, self.incluBlendTime or 4) end
    if should_compat then compat(paths) end
    events.TICK:register(tick)
    init = true
end}

local function addAllAnimsController(...)
    for _, v in ipairs{...} do
        assert(
            type(v) == "Animation",
            "§aCustom Script Warning: §6addAllAnimsController was given something that isn't an animation, check its spelling for errors.§c")
      allAnims[#allAnims+1] = v
    end
end

local function addExcluAnimsController(...)
    for _, v in ipairs{...} do
        assert(
            type(v) == "Animation",
            "§aCustom Script Warning: §6addExcluAnimsController was given something that isn't an animation, check its spelling for errors.§c")
      excluAnims[#excluAnims+1] = v
    end
end

local function addIncluAnimsController(...)
    for _, v in ipairs{...} do
        assert(
            type(v) == "Animation",
            "§aCustom Script Warning: §6addIncluAnimsController was given something that isn't an animation, check its spelling for errors.§c")
      incluAnims[#incluAnims+1] = v
    end
end

-- If you're choosing to edit this script, don't put anything beneath the return line

return setmetatable(
    {
        animsList = animsList,
        addAllAnimsController = addAllAnimsController,
        addExcluAnimsController = addExcluAnimsController,
        addIncluAnimsController = addIncluAnimsController
    },
    animMT
)