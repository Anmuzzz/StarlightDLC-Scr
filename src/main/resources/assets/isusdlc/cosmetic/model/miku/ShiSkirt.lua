local shiSkirt=models.model.root.Torso.Body.skirt --Skirt (the other parts branch off of this variable, if you named the bb parts the same as mine, then you only need to set this one up)
local shiSkirtFront=shiSkirt.Front --SkirtFront
local shiSkirtFrontCenter=shiSkirt.Front.FrontCenter --SkirtFrontCenter
local shiSkirtFrontLeft=shiSkirt.Front.FrontLeft --SkirtFrontRight
local shiSkirtFrontRight=shiSkirt.Front.FrontRight --SkirtFrontLeft
local shiSkirtBack=shiSkirt.Back --SkirtBack
local shiSkirtBackCenter=shiSkirt.Back.BackCenter --SkirtBackCenter
local shiSkirtBackLeft=shiSkirt.Back.BackLeft --SkirtBackRight
local shiSkirtBackRight=shiSkirt.Back.BackRight --SkirtBackLeft
local shiSkirtLeft=shiSkirt.SkirtLeft --Skirt left part
local shiSkirtRight=shiSkirt.SkirtRight --Skirt right part
local shiYDis=0
local shiVY=0
local shiVYStiffX=.1
function events.render()
    local shiVY_raw= math.clamp(((player:getVelocity().y)*60),-20,0)

    --local shiLLegRot = vanilla_model.LEFT_LEG:getOriginRot().x / 3
    --local shiRLegRot = vanilla_model.RIGHT_LEG:getOriginRot().x / 3

    local shiLLegRot = models.model.root.LeftLeg:getAnimRot().x / 2
    local shiRLegRot = models.model.root.RightLeg:getAnimRot().x / 2

    local shiSkirtKick = shiLLegRot - shiRLegRot
    local shiSkirtKickABS = math.abs(shiSkirtKick)
    if not player:isGliding() then
        shiYDis=(shiVY-shiVY_raw)
        shiVY=math.clamp(shiVY-shiVYStiffX*shiYDis,-30,0)
    else
        shiVY=0
    end
    shiSkirtFront:setRot(math.clamp(shiSkirtKickABS,0,5)-shiVY,0,0)
    shiSkirtFrontCenter:setRot((shiSkirtKickABS/4)-shiVY,-shiSkirtKick/10,-shiSkirtKick/5)
    shiSkirtFrontRight:setRot((shiSkirtKickABS/5)-shiVY/2,shiSkirtKick/10,math.clamp(shiSkirtKick/8,0,90)-shiVY)
    shiSkirtFrontLeft:setRot((shiSkirtKickABS/5)-shiVY/2,shiSkirtKick/10,math.clamp(shiSkirtKick/8,-90,0)+shiVY)
    shiSkirtBack:setRot(-math.clamp(shiSkirtKickABS,0,5)+shiVY,0,0)
    shiSkirtBackCenter:setRot((-shiSkirtKickABS/4)-shiVY,-shiSkirtKick/10,shiSkirtKick/5)
    shiSkirtBackRight:setRot(-(shiSkirtKickABS/5)-shiVY/2,-shiSkirtKick/10,math.clamp(shiSkirtKick/8,0,90)-shiVY)
    shiSkirtBackLeft:setRot(-(shiSkirtKickABS/5)-shiVY/2,-shiSkirtKick/10,math.clamp(shiSkirtKick/8,-90,0)+shiVY)
    shiSkirtRight:setRot(0,-shiSkirtKick/10,-math.clamp(shiSkirtKickABS/8,-90,0)-shiVY)
    shiSkirtLeft:setRot(0,-shiSkirtKick/10,math.clamp(shiSkirtKickABS/8,-90,0)+shiVY)
    if player:isCrouching() then
        shiSkirt:setRot(20,0,0)
    else
        shiSkirt:setRot()
    end
end