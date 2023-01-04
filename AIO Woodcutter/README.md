CLI:

NOTE: You will still need to select trees after loading in game.

"." between option and choice.
bank_logs | true/false
loot_nests | true/false
afk_mode | true false
if afk_mode is true:
min_sleep | integer (e.g. 4000)
max_sleep | integer higher than min (e.g. 8000)

Example:
-script "Fury AIO Woodcutter":bank_logs.true.loot_nests.true.afk_mode.true.min_sleep.4000.max_sleep.8000