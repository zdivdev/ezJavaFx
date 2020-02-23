
from javafx.application import Application
from javafx.scene import Scene
from javafx.scene import Node
from javafx.scene.layout import VBox
from javafx.scene.layout import HBox
from javafx.scene.layout import Priority
from javafx.geometry import Insets
from javafx.geometry import Orientation
from javafx.geometry import Pos

#
# Control Table
#

__ctrl_table = {}

#
# Dialog
#

def Alert(title, message, stage=None):
    from javafx.scene.control import Alert
    from javafx.scene.control.Alert import AlertType
    from javafx.stage import Modality
    alert = Alert(AlertType.INFORMATION)
    alert.setTitle(title)
    alert.setHeaderText("")
    alert.setContentText(message)
    if stage: alert.initOwner(stage)
    alert.initModality(Modality.APPLICATION_MODAL)
    alert.showAndWait()

def YesNo(title, message, stage=None):
    from javafx.scene.control import Alert
    from javafx.scene.control.Alert import AlertType
    from javafx.stage import Modality
    from javafx.scene.control import ButtonType
    alert = Alert(AlertType.CONFIRMATION)
    alert.setTitle(title)
    alert.setHeaderText("")
    alert.setContentText(message)
    alert.initModality(Modality.APPLICATION_MODAL)
    if stage: alert.initOwner(stage)
    result = alert.showAndWait()
    if result.get() == ButtonType.OK:
        return True
    else:
        return False

def FileDialog(initialFile, save, stage=None):
    from javafx.stage import FileChooser
    from java.io import File
    dlg = FileChooser()
    if initialFile:
        f = File(initialFile)
        if f.exists():
            if f.isDirectory(): dlg.setInitialDirectory(f)
            if f.isFile():      dlg.setInitialFileName(f.getAbsolutePath());
    dlg.setTitle("Select File");
    if save: return dlg.showSaveDialog(stage);
    else:    return dlg.showOpenDialog(stage);

def FileOpenDialog(initialFile, stage=None):
    return FileDialog(initialFile, False, stage)

def FileSaveDialog(initialFile, stage=None):
    return FileDialog(initialFile, True, stage)

def DirectoryOpenDialog(initialDirectory, stage=None):
    from javafx.stage import DirectoryChooser
    from java.io import File
    dlg = DirectoryChooser()
    if initialDirectory:
        f = File(initialDirectory)
        if f.exists() and f.isDirectory():
            dlg.setInitialDirectory(f);
    dlg.setTitle("Select Folder");
    return dlg.showDialog(stage)

#
# Container
#

class FxBox():
    def getItem(self,index): return self.ctrl.getChildren().get(index)
    def addItem(self,item,expand=False): 
        self.ctrl.getChildren().add(item)
        if expand == True: self.setExpand(item)
    def alignLeft(self): self.setAlignLeft()
    def alignRight(self): self.setAlignRight()
    def addSeparator(self):
        from javafx.scene.control import Separator
        sep = Separator()
        sep.setOrientation(self.separatorOrientation)
        self.ctrl.getChildren().add( sep )

class FxVBox(FxBox):
    def __init__(self,gap=0,pad=0):
        self.ctrl = VBox( gap )
        self.setExpand = lambda x : VBox.setVgrow(x, Priority.ALWAYS)
        self.setAlignLeft = lambda : self.ctrl.setAlignment(Pos.CENTER_LEFT)
        self.setAlignRight = lambda : self.ctrl.setAlignment(Pos.CENTER_RIGHT)
        self.separatorOrientation = Orientation.HORIZONTAL
        self.ctrl.setAlignment(Pos.CENTER)
        self.ctrl.setSpacing( gap )
        self.ctrl.setPadding( Insets( pad, pad, pad, pad ) )

class FxHBox(FxBox):
    def __init__(self,gap=0,pad=0):
        self.ctrl = HBox( gap )
        self.setExpand = lambda x : HBox.setHgrow(x, Priority.ALWAYS)
        self.setAlignLeft = lambda : self.ctrl.setAlignment(Pos.CENTER_LEFT)
        self.setAlignRight = lambda : self.ctrl.setAlignment(Pos.CENTER_RIGHT)
        self.separatorOrientation = Orientation.VERTICAL
        self.ctrl.setAlignment(Pos.CENTER)
        self.ctrl.setSpacing( gap )
        self.ctrl.setPadding( Insets( pad, pad, pad, pad ) )


class FxBorderPane():
    def __init__(self,w):
        from javafx.scene.layout import BorderPane
        from javafx.geometry import Insets
        self.ctrl = BorderPane()
        self.ctrl.setPadding(Insets(0, 0, 0, 0))
        if w.content: self.setCenter(FxLayout(w.content))
        v = FxVBox()
        if w.menu: v.addItem(FxMenuBar(w.menu))
        if w.tool: v.addItem(FxToolBar(w.tool))
        self.setTop(v.ctrl)
    def setTop(self,item):    self.ctrl.setTop(item)
    def setBottom(self,item): self.ctrl.setBottom(item)
    def setLeft(self,item):   self.ctrl.setLeft(item)
    def setRight(self,item):  self.ctrl.setRight(item)
    def setCenter(self,item): self.ctrl.setCenter(item)
 
class FxSplitPane(object):
    def addItem(self,item): self.ctrl.getItems().add(item)
    def addItems(self,layout):
        items = layout.get('items')
        if items:
            for item in items:
                self.addItem(FxLayout(item))
        
class FxVSplitPane(FxSplitPane):
    def __init__(self,h):
        from javafx.scene.control import SplitPane
        self.ctrl = SplitPane()
        if h.get('first'):
            self.ctrl.setDividerPositions(h['first'], 1-h['first']);
        self.ctrl.setOrientation(Orientation.VERTICAL);
        self.addItems(h)

class FxHSplitPane(FxSplitPane):
    def __init__(self,h):
        from javafx.scene.control import SplitPane
        self.ctrl = SplitPane()
        if h.get('first'):
            self.ctrl.setDividerPositions(h['first'], 1-h['first']);
        self.ctrl.setOrientation(Orientation.HORIZONTAL);
        self.addItems(h)

class FxTabPane():
    def __init__(self,h):
        from javafx.scene.control import TabPane
        self.ctrl = TabPane()
        labels = h.get('labels')
        items = h.get('items')
        if labels and items:
            for i in range(0,len(items)):
                self.addItem( labels[i], FxLayout(items[i]))        
    def addItem(self, title, item):
        from javafx.scene.control import Tab
        tab = Tab()
        tab.setText(title)
        tab.setContent(item)
        self.ctrl.getTabs().add(tab)
              
#
# Control
#

class FxLabel():
    def __init__(self,h):
        from javafx.scene.control import Label
        self.ctrl = Label()
        self.ctrl.setText(h.get('label'))
        #ctrl.setAlignment(Pos.CENTER);
        
class FxButton():
    def __init__(self,h):
        from javafx.scene.control import Button
        from javafx.scene.control import Tooltip
        self.ctrl = Button()
        self.ctrl.setText(h.get('label'))
        if h.get('tooltip'): self.ctrl.setTooltip(Tooltip(h['tooltip']))
        if h.get('handler'): self.ctrl.setOnAction( h['handler'] )

class FxText():
    def getText(self): return self.ctrl.getText()
    def setText(self,text): self.ctrl.setText(text)
    def insert(self,index,text): self.ctrl.insertText(index,text)
    def append(self,text): self.ctrl.appendText(text)
    def clear(self): self.ctrl.clear()
    def copy(self): self.ctrl.copy()
    def paste(self): self.ctrl.paste()
        
class FxTextField(FxText):
    def __init__(self,h):
        from javafx.scene.control import TextField
        self.ctrl = TextField()
        self.ctrl.setText(h.get('text'))
        SetFileDropHandler( self.ctrl, self.DropHandler )
    def DropHandler(self,files):
        self.ctrl.setText( files.get(0).getPath() )
        
class FxTextArea(FxText):
    def __init__(self,h):
        from javafx.scene.control import TextArea
        self.ctrl = TextArea()
        self.ctrl.setText(h.get('text'))

#
# Window
#

def DragOver(event):
    from javafx.scene.input import TransferMode;
    if event.getDragboard().hasFiles():
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
    event.consume();

def DragDropped(handler):
    from javafx.scene.input import DragEvent
    from javafx.scene.input import Dragboard
    def _DragDropped(event): #closure
        db = event.getDragboard()
        if db.hasFiles():
            handler( db.getFiles() ) # List<File>
            event.setDropCompleted(True)
        else:
            event.setDropCompleted(False)
        event.consume()
    return _DragDropped

def SetFileDropHandler(ctrl,handler):  
    ctrl.setOnDragOver( DragOver )
    ctrl.setOnDragDropped( DragDropped(handler) )

def FxMenu(name,menu_table):
    from javafx.scene.control import Menu
    from javafx.scene.control import MenuBar
    from javafx.scene.control import MenuItem
    from javafx.scene.control import SeparatorMenuItem
    menu = Menu(name)
    for m in menu_table:
        if not m.get('name'): continue # Separater
        if not m.get('item'): continue # Disabled
        if type(m['item']) == list:
            menu.getItems().add(FxMenu(m['name'],m['item']))
        else:
            item = MenuItem(m['name'])
            item.setOnAction(m['item'])
            menu.getItems().add(item);
    return menu

def FxMenuBar(menubar_table):
    from javafx.scene.control import Menu
    from javafx.scene.control import MenuBar
    from javafx.scene.control import MenuItem
    from javafx.scene.control import SeparatorMenuItem
    menubar = MenuBar()
  
    for m in menubar_table:
        if m.get('name'):
            menubar.getMenus().add(FxMenu(m['name'],m['item']))
        else:
            if m.get('fontsize'):
                menubar.setStyle("-fx-font: " + m['fontsize'] + " arial;")  
    return menubar

def FxToolBar(toolbar_table):
    from javafx.scene.control import Separator;
    from javafx.scene.control import ToolBar;
    toolbar = ToolBar();
    for m in toolbar_table:
        if not m.get('name'): continue # Separater
        if not m.get('handler'): continue # Disabled
        item = FxButton(m)
        toolbar.getItems().add(item.ctrl);
    return toolbar

'''
    public void addToolBarItem(Node item, boolean extend) {
        if( item == null ) {
            currToolBar.getItems().add(new Separator());
        } else {
            VBox.setVgrow(currToolBar, Priority.NEVER);
            if( extend ) {
                HBox.setHgrow(item, Priority.ALWAYS);   
            }
            currToolBar.getItems().add(item);
        }
    }   
    
    public void addSeparator() {
        currToolBar.getItems().add(new Separator());
    }
'''

def FxLayout(content):
    vbox = FxVBox(1,1)
    for v in content:
        hbox = FxHBox(1,1)
        expand = False
        for h in v:
            name = h.get('name')
            if not name:
                if h.get('expand'): expand = h['expand']
                continue
            if   name == 'Label':     f = FxLabel(h)
            elif name == 'Button':    f = FxButton(h)
            elif name == 'TextField': f = FxTextField(h)
            elif name == 'TextArea':  f = FxTextArea(h)
            elif name == 'HSplit':    f = FxHSplitPane(h)
            elif name == 'VSplit':    f = FxVSplitPane(h)
            elif name == 'TabPane':   f = FxTabPane(h)
            else: continue
            
            width = -1
            height = -1
            if h.get('width') : width  = h['width']
            if h.get('height'): height = h['height']
            f.ctrl.setPrefSize(width, height);

            if h.get('fontsize'):
                f.ctrl.setStyle("-fx-font: " + h['fontsize'] + " arial;");                
            if h.get('tooltip'):
                from javafx.scene.control import Tooltip
                f.ctrl.setTooltip(Tooltip(h['tooltip']))
            if h.get('key'):
                __ctrl_table[h['key']] = f
            hbox.addItem(f.ctrl,expand=h.get('expand'))
        vbox.addItem(hbox.ctrl,expand=expand)
    return vbox.ctrl

def getCtrl(name):
    return __ctrl_table.get(name)
    
class Window(Application):
    def start(self, stage):
        from javafx.application import Platform
        self.ctrl  = __ctrl_table
        self.stage = stage
        self.stage.setTitle("FxApp Example")
        if self.closeHandler: self.stage.setOnCloseRequest(self.closeHandler)
        Platform.setImplicitExit(True)
        pane = FxBorderPane(self)
        self.scene = Scene(pane.ctrl, 640, 400)
        self.stage.setScene(self.scene)
        self.stage.show()
    def SetCloseHandler(self,handler): self.closeHandler = handler
    def Close(self): self.stage.close()
    def SetContent(self,content): self.content = content
    def Alert(self, title, message): Alert(title,message,self.stage)
    def YesNo(title, message, stage=None): return YesNo(title,message,self.stage)
    def FileOpenDialog(self, initialFile): return FileOpenDialog(initialFile, self.stage)
    def FileSaveDialog(self, initialFile): return FileSaveDialog(initialFile, self.stage)
    
    
#
# Application
#

      
class FxApp(Window):
    def __init__(self):
        self.menu = [
            { 'name' : "File",
              'item' : [
                    { 'name' : "Exit" , 'item' : self.onExit, 'icon' : 'exit' } ]
            }, { 'name' : "Help",
              'item' : [
                    { 'name' : "About", 'item' : self.onAbout, 'icon' : 'help' } ]
            }]
        self.tool = [
                { "name" : "Button",  "label" : "Exit", "handler" : self.onExit, "tooltip" : "Quit"  },
            ]
        tab1 = [[ { "name" : "TextArea", "expand" : True },
                  { "expand" : True }, ]]
        tab2 = [[ { "name" : "TextArea", "expand" : True },
                  { "expand" : True }, ]]   
        split1 = [[ { "name" : "TextArea", "expand" : True },
                    { "expand" : True }, ]]
        split2 = [[ { "name" : "TextArea", "expand" : True },
                    { "expand" : True }, ]]   
        self.content = [ # vbox
            [ # hbox
                { "name" : "Label", "label" : "Address:" },
                { "name" : "TextField", "key" : "text", "expand" : True },
                { "name" : "Button",  "label" : "Browse", "tooltip" : "Open File", "handler" : self.onBrowse  },
                { "name" : "Button",  "label" : "About", "handler" : self.onAbout  },
            ],
            [ # hbox
                { "name" : "TextArea", "expand" : True },
                { "expand" : True },
            ],
            [ # hbox
                { "name" : "TabPane", "labels" : [ "Tab1", "Tab2" ], "items" : [ tab2, tab2 ], "expand" : True },
                { "expand" : True },
            ],  
            [ # hbox
                { "name" : "HSplit", "items" : [ split1, split2 ] , "first" : 0.5, "expand" : True},
                { "expand" : True },
            ],                
        ]
        self.SetCloseHandler(self.onClose)
    def onAbout(self,event):
        v = YesNo("Global", "Dialog")
        if v: self.Alert("Result", "Yes")
        else: self.Alert("Result", "No")
    def onBrowse(self,event):
        f = FileOpenDialog(None)
        ctrl = getCtrl('text')
        if ctrl:                
            ctrl.setText(f.getPath())
    def onExit(self,event):
        from javafx.application import Platform
        Platform.exit()
        #from java.lang import System
        #ASystem.exit(0)
        #self.Close()
    def onClose(self,event):
        v = YesNo("Alert", "Do you want to quit ?", self.stage)
            
if __name__ == '__main__':
    Application.launch(FxApp().class, [])
