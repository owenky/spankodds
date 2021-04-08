/*	 super();
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
	int lastIndex=thispane.indexOfTab("+");
        if(!dragging) {
          // Gets the tab index based on the mouse position
          int tabNumber = getUI().tabForCoordinate(SportsTabPane.this, e.getX(), e.getY());
		  if(tabNumber==lastIndex-1)
		  {
			  currentTabIndex--;
			  System.out.println(currentTabIndex+"mouseDragged"+(currentTabIndex == getTabCount() -1));
		  }
			  

          if(tabNumber >= 0&&tabNumber<lastIndex) {
            draggedTabIndex = tabNumber;
           // Rectangle bounds = getUI().getTabBounds(SportsTabPane.this, tabNumber);


            // Paint the tabbed pane to a buffer
            //Image totalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            //Graphics totalGraphics = totalImage.getGraphics();
            //totalGraphics.setClip(bounds);
            // Don't be double buffered when painting to a static image.
           // setDoubleBuffered(false);
            //paintComponent(totalGraphics);

            // Paint just the dragged tab to the buffer
           // tabImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
           //Graphics graphics = tabImage.getGraphics();
           // graphics.drawImage(totalImage, 0, 0, bounds.width, bounds.height, bounds.x, bounds.y, bounds.x + bounds.width, bounds.y+bounds.height, SportsTabPane.this);

            dragging = true;
            repaint();
          }
        } else {
          currentMouseLocation = e.getPoint();

          // Need to repaint
          repaint();
        }

        super.mouseDragged(e);
      }
    });

    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
		int lastIndex=thispane.indexOfTab("+");
        if(dragging) {
          int tabNumber = getUI().tabForCoordinate(SportsTabPane.this, e.getX(), 10);
		 if(tabNumber==lastIndex-1)
		  {
			 currentTabIndex--;
			  System.out.println(currentTabIndex+"mouseReleased"+(currentTabIndex == getTabCount() -1));
		  }
          if(tabNumber >= 0&&tabNumber<lastIndex) {
            Component comp = getComponentAt(draggedTabIndex);
            String title = getTitleAt(draggedTabIndex);
            removeTabAt(draggedTabIndex);
			String img="";
			if(title.equals("Football"))
			{
				img="football.png";
			}
			else if(title.equals("Basketball"))
			{
				img="basketball.png";
			}
			else if(title.equals("Baseball"))
			{
				img="Baseball.png";
			}
			else if(title.equals("Hockey"))
			{
				img="Hockey.png";
			}
			else if(title.equals("Fighting"))
			{
				img="boxing.png";
			}
			else if(title.equals("Soccer"))
			{
				img="Soccer.png";
			}
			else if(title.equals("Auto Racing"))
			{
				img="flag.png";
			}
			else if(title.equals("Golf"))
			{
				img="Golf.png";
			}
			else if(title.equals("Tennis"))
			{
				img="Tennis.png";
			}
			else if(title.equals("Today")){
				img="Today.png";
			}
			else{
				
			}
			
            insertTab(title, new ImageIcon(img), new MainScreen(title), title, tabNumber);
          }
        }

        dragging = false;
        tabImage = null;
		
      }
    });*/