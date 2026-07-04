# Changelog

## 2.5-beta

### Добавлено
- **28 новых модулей** из Exosware:
  - Combat: Criticals, HitBox, NoFriendDamage, TargetStrafe
  - Movement: AirStuck, NoWeb, SuperFirework, Timer, Strafe, ElytraMotion, ElytraRecast, ElytraTarget, ElytraHelper
  - Player: AutoLeave, AutoRespawn, AutoTool, GuiWalk, NoPush, MiddleClickPearl, NoDelay, FreeCamera
  - Other: AutoDuel, NoCommands, UnHook
  - Visuals: FullBright, CrossHair, NoRender, BlockESP
- **Косметическая система** (портирована из Exosware):
  - BBmodel-рендерер с поддержкой Blockbench-анимаций
  - 3 вида косметики: модели, питомцы, аксессуары
  - GUI выбора косметики с поиском и 3D-превью
  - Mixin для замены модели игрока
  - Mixin для замены предметов в руке (лук/меч)
  - Встроенные модели: miku, teto, goose, howlpendragon, kaltist, repo, starhorm, simplewings, allay, birb, mothli
- **Описания модулям**: AutoEat, Auto Sprint, Custom Fog, Interface, Prediction, Trap ESP, Waypoints

### Удалено
- Lua-система (ScriptManager, LuaModule, LuaScriptScreen, LuaJ зависимость)
- AutoBuy
- PlaceFarm
- Кнопка "Secret" в главном меню

### Изменено
- Название jar: `StarLightDLC-2.5-beta.jar`
- Java 21 для Gradle сборки (исправление совместимости с Java 26)
- Исправлен баг с double-экземпляром CosmeticModule (singleton теперь корректно регистрируется в ModuleManager)
