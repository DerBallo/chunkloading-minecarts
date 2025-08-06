# Chunkloading Minecarts

Make minecarts act as chunk loaders using redstone.

## How to use it?

***Minecarts will only load chunks after you manually (or by using redstone) enabled them.***

First create a "chunk loader creator" by putting the following pattern in a dispenser. When powered, any minecart in front of it will toggle its chunk loading ability. If the minecart is an active chunkloader, it will emit particles.

![Image unavailable](https://i.imgur.com/bG5sBpA.png)

## Customization

You can change the radius in which chunks around the minecart will be loaded by changing the "minecartTicketRadius" gamerule to any value between 1 and 16, 1 meaning only the current chunk the minecart is in will be loaded.

A chunk loading ticket created by a minecart will always expire 5 ticks after it's creation and will only be refreshed once expired.