

--Replace these hex colours with your own to customize your goose's accessories. If you want to retexture the accessories on your own, delete this file!--


local NeckBowColour = "#db1f25"
--The bow on the neck

local HeadBowColour = "#db1f25"
--The bow on the head

local BucketMainColour = "#45d988"
--The bucket's main body

local BucketSecondaryColour = "#e8d774"
--The bucket's handle and stripe

local TophatMain = "#2f2d3d"
--The main body of the tophat

local TophatSecondary = "#e82048"
--The ring of the tophat

local Necktie = "#2f2d3d"
--Necktie


----------DO NOT EDIT BELOW THIS LINE!!! Unless you know what you're doing :D-----------

local model = models.goose


--neckbow
model.root.gorso.gneck1.neckaccessories.neckbow:setColor(vectors.hexToRGB(NeckBowColour))

--headbow
model.root.gorso.gneck1.gneck2.head.headaccessories.headbow:setColor(vectors.hexToRGB(HeadBowColour))

--bucketmain

model.root.gorso.gneck1.gneck2.head.jaw.accessoriesjaw.bucket.bucketmain.bucketbody:setColor(vectors.hexToRGB(BucketMainColour))

--bucketsecondary
model.root.gorso.gneck1.gneck2.head.jaw.accessoriesjaw.bucket.buckettsecond:setColor(vectors.hexToRGB(BucketSecondaryColour))
model.root.gorso.gneck1.gneck2.head.jaw.accessoriesjaw.bucket.bucketmain.buckettsecondring:setColor(vectors.hexToRGB(BucketSecondaryColour))

--tophat
model.root.gorso.gneck1.gneck2.head.headaccessories.tophat.tophatmain:setColor(vectors.hexToRGB(TophatMain))

--tophat secondary
model.root.gorso.gneck1.gneck2.head.headaccessories.tophat.tophatring:setColor(vectors.hexToRGB(TophatSecondary))

--necktie
model.root.gorso.gneck1.neckaccessories.necktie:setColor(vectors.hexToRGB(Necktie))

