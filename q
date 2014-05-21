[33mcommit 38047df5c0d270e6e85812fa6818345effd39f1e[m
Author: Dsmilyanov <dsmilyanov@gmail.com>
Date:   Tue May 20 23:15:14 2014 +0300

    Added background

[1mdiff --git a/bin/spaceinvaders/Board$TAdapter.class b/bin/spaceinvaders/Board$TAdapter.class[m
[1mindex c4a0c51..1549cb8 100644[m
Binary files a/bin/spaceinvaders/Board$TAdapter.class and b/bin/spaceinvaders/Board$TAdapter.class differ
[1mdiff --git a/bin/spaceinvaders/Board.class b/bin/spaceinvaders/Board.class[m
[1mindex ab78f9d..8118335 100644[m
Binary files a/bin/spaceinvaders/Board.class and b/bin/spaceinvaders/Board.class differ
[1mdiff --git a/bin/spacepix/Thumbs.db b/bin/spacepix/Thumbs.db[m
[1mnew file mode 100644[m
[1mindex 0000000..2deda87[m
Binary files /dev/null and b/bin/spacepix/Thumbs.db differ
[1mdiff --git a/bin/spacepix/bground.png b/bin/spacepix/bground.png[m
[1mnew file mode 100644[m
[1mindex 0000000..1429ff4[m
Binary files /dev/null and b/bin/spacepix/bground.png differ
[1mdiff --git a/spacepix/Thumbs.db b/spacepix/Thumbs.db[m
[1mnew file mode 100644[m
[1mindex 0000000..d5a9997[m
Binary files /dev/null and b/spacepix/Thumbs.db differ
[1mdiff --git a/src/spaceinvaders/Board.java b/src/spaceinvaders/Board.java[m
[1mindex e7bb2fe..7781363 100644[m
[1m--- a/src/spaceinvaders/Board.java[m
[1m+++ b/src/spaceinvaders/Board.java[m
[36m@@ -128,18 +128,16 @@[m [mpublic class Board extends JPanel implements Runnable, Commons {[m
     public void paint(Graphics g)[m
     {[m
       super.paint(g);[m
[31m-<<<<<<< HEAD[m
 [m
       g.setColor(Color.black);[m
       g.fillRect(0, 0, d.width, d.height);[m
[31m-      g.setColor(Color.blue);   [m
[31m-=======[m
[32m+[m[32m      g.setColor(Color.blue);[m
[32m+[m[41m      [m
       [m
       //TODO we don't need the ground - remove it later?!?[m
       g.setColor(Color.white); //changed from black[m
       g.fillRect(0, 0, d.width, d.height); //changed (0, 0, d.width, d.height)[m
[31m-      g.setColor(Color.green);   [m
[31m->>>>>>> mainRepo/master[m
[32m+[m[32m      g.setColor(Color.green);[m
 [m
       if (ingame) {[m
 [m
[1mdiff --git a/src/spacepix/Thumbs.db b/src/spacepix/Thumbs.db[m
[1mnew file mode 100644[m
[1mindex 0000000..2deda87[m
Binary files /dev/null and b/src/spacepix/Thumbs.db differ
