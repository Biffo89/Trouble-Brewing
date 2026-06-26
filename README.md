# Trouble Brewing
This plugin displays information about the number of pieces of eight earned during a Trouble Brewing game.
Players will earn up to 100 pieces of eight through contribution, and a further 10 for every bottle of rum their team
produces. Although the OSRS wiki states that players must contribute to be eligible for pieces of eight from rum,
through personal testing, I have found that players may receive pieces of eight with no contribution at all.

# Brew Stats
Enabling Brew Stats will show more information about the current game state.

All five ingredients are listed with the current amount followed by the maximum that will be consumed before the end of the game.  For example, Bitternuts 3 (12) indicates you currently have 3 bitternuts in stock and 12 is the maximum amount of bitternut you could consume before the end of the game.  Once the stock matches or exceeds the maximum, the count will turn green.

Rum Eligible Level indicates how many rums you are allowed to load for scoring.  This is an anti-AFK measure so only players participating in rum production can score for the team.  You get one eligibility for one bitternut, one sweetgrubs, five buckets of water, three colored water, OR one scrapey bark.  However, do note that currently the eligibility appears to decrement more aggressively than one per bottle created.  If your eligibility level is zero when the rum finishes, you will not be able to see or interact with the converyor belt.

Brew time is the amount of time in seconds before the rum is completed.  This is a 38 second counter from when stored ingredients decrement.  This timer will run negative up to -30 seconds to show lapses in brewing time.  Some ingredient must be added to their hopper or a log must be added to a boiler to kick start the next brewing cycle.  The wiki claims it will automatically start 20 seconds after a brew cycle is complete; however, I have seen it stall for even longer if no activity is occuring.

Rum State shows when a brew bottle is ready for scoring.  This may not update for you if you are not eligibile to see rum.

Game Time indicates the amount of time remaining in the game.