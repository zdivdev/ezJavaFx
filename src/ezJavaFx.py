
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

def DumpControlTable():
    for k,v in __ctrl_table.items():
        print(k,v)
        
#
# Dialog
#

def EzAlert(title, message, stage=None):
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

def EzYesNo(title, message, stage=None):
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

def EzFileDialog(initialFile, save, stage=None):
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

def EzFileOpenDialog(initialFile, stage=None):
    return EzFileDialog(initialFile, False, stage)

def EzFileSaveDialog(initialFile, stage=None):
    return EzFileDialog(initialFile, True, stage)

def EzDirectoryOpenDialog(initialDirectory, stage=None):
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

class EzBox():
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

class EzVBox(EzBox):
    def __init__(self,gap=0,pad=0):
        self.ctrl = VBox( gap )
        self.setExpand = lambda x : VBox.setVgrow(x, Priority.ALWAYS)
        self.setAlignLeft = lambda : self.ctrl.setAlignment(Pos.CENTER_LEFT)
        self.setAlignRight = lambda : self.ctrl.setAlignment(Pos.CENTER_RIGHT)
        self.separatorOrientation = Orientation.HORIZONTAL
        self.ctrl.setAlignment(Pos.CENTER)
        self.ctrl.setSpacing( gap )
        self.ctrl.setPadding( Insets( pad, pad, pad, pad ) )

class EzHBox(EzBox):
    def __init__(self,gap=0,pad=0):
        self.ctrl = HBox( gap )
        self.setExpand = lambda x : HBox.setHgrow(x, Priority.ALWAYS)
        self.setAlignLeft = lambda : self.ctrl.setAlignment(Pos.CENTER_LEFT)
        self.setAlignRight = lambda : self.ctrl.setAlignment(Pos.CENTER_RIGHT)
        self.separatorOrientation = Orientation.VERTICAL
        self.ctrl.setAlignment(Pos.CENTER)
        self.ctrl.setSpacing( gap )
        self.ctrl.setPadding( Insets( pad, pad, pad, pad ) )


class EzBorderPane():
    def __init__(self,w):
        from javafx.scene.layout import BorderPane
        from javafx.geometry import Insets
        self.ctrl = BorderPane()
        self.ctrl.setPadding(Insets(0, 0, 0, 0))
        if w.content: self.setCenter(EzLayout(w.content))
        v = EzVBox()
        if w.menu: v.addItem(EzMenuBar(w.menu))
        if w.tool: v.addItem(EzToolBar(w.tool))
        self.setTop(v.ctrl)
    def setTop(self,item):    self.ctrl.setTop(item)
    def setBottom(self,item): self.ctrl.setBottom(item)
    def setLeft(self,item):   self.ctrl.setLeft(item)
    def setRight(self,item):  self.ctrl.setRight(item)
    def setCenter(self,item): self.ctrl.setCenter(item)
 
class EzSplitPane(object):
    def addItem(self,item): self.ctrl.getItems().add(item)
    def addItems(self,layout):
        items = layout.get('items')
        if items:
            for item in items:
                self.addItem(EzLayout(item))
        
class EzVSplitPane(EzSplitPane):
    def __init__(self,h):
        from javafx.scene.control import SplitPane
        self.ctrl = SplitPane()
        if h.get('first'):
            self.ctrl.setDividerPositions(h['first'], 1-h['first']);
        self.ctrl.setOrientation(Orientation.VERTICAL);
        self.addItems(h)

class EzHSplitPane(EzSplitPane):
    def __init__(self,h):
        from javafx.scene.control import SplitPane
        self.ctrl = SplitPane()
        if h.get('first'):
            self.ctrl.setDividerPositions(h['first'], 1-h['first']);
        self.ctrl.setOrientation(Orientation.HORIZONTAL);
        self.addItems(h)

class EzTabPane():
    def __init__(self,h):
        from javafx.scene.control import TabPane
        self.ctrl = TabPane()
        labels = h.get('labels')
        items = h.get('items')
        if labels and items:
            for i in range(0,len(items)):
                self.addItem( labels[i], EzLayout(items[i]))        
    def addItem(self, title, item):
        from javafx.scene.control import Tab
        tab = Tab()
        tab.setText(title)
        tab.setContent(item)
        self.ctrl.getTabs().add(tab)
              
#
# Control
#

class EzControl():
    def SetBackground(self,color): # {D8BFD8}
        self.ctrl.setStyle("-fx-background-color: #" + color + ";")
    def SetFontSize(self,size):
        self.ctrl.setStyle("-fx-font-size: " + str(size) + ";")
    def SetIcon(self,icon,top=False):
        from java.io import FileInputStream
        from javafx.scene.image import Image
        from javafx.scene.image import ImageView
        from javafx.scene.control import ContentDisplay;
        self.ctrl.setGraphic(ImageView(Image(FileInputStream(icon))))
        if top: self.ctrl.setContentDisplay(ContentDisplay.TOP);

class EzLabel(EzControl):
    def __init__(self,h):
        from javafx.scene.control import Label
        self.ctrl = Label()
        self.ctrl.setText(h.get('label'))
        if h.get('icon'): self.SetIcon( h['icon'] )
        #ctrl.setAlignment(Pos.CENTER);
        
class EzButton(EzControl):
    def __init__(self,h):
        from javafx.scene.control import Button
        self.ctrl = Button()
        self.ctrl.setText(h.get('label'))
        if h.get('handler'): self.ctrl.setOnAction( h['handler'] )
        if h.get('icon'): self.SetIcon( h['icon'], True )

class EzToggleButton(EzControl):
    def __init__(self,h):
        from javafx.scene.control import ToggleButton
        self.ctrl = ToggleButton()
        self.ctrl.setText(h.get('label'))
        if h.get('handler'): self.ctrl.setOnAction( h['handler'] )
        if h.get('icon'): self.SetIcon( h['icon'] )
    def IsSelected(self):
        return self.ctrl.isSelected()

class EzChoiceBox(EzControl):
    def __init__(self,h):
        from javafx.beans.value import ChangeListener
        from javafx.beans.value import ObservableValue
        from javafx.scene.control import ChoiceBox
        self.ctrl = ChoiceBox()
        if h.get('items'): self.ctrl.getItems().addAll( h['items'] );
        self.ctrl.getSelectionModel().selectFirst()    
    def Add(self,item):
        self.ctrl.getItems().add(item);
    def GetSelectedItem(self):
        return self.ctrl.getSelectionModel().getSelectedItem()
    def GetSelectedIndex(self):
        return self.ctrl.getSelectionModel().getSelectedIndex()


class EzComboBox(EzControl):
    def __init__(self,h):
        from javafx.beans.value import ChangeListener
        from javafx.beans.value import ObservableValue
        from javafx.scene.control import ComboBox
        self.ctrl = ComboBox()
        if h.get('items'): self.ctrl.getItems().addAll( h['items'] );
        self.ctrl.getSelectionModel().selectFirst()    
    def Add(self,item):
        self.ctrl.getItems().add(item);
    def GetSelectedItem(self):
        return self.ctrl.getSelectionModel().getSelectedItem()
    def GetSelectedIndex(self):
        return self.ctrl.getSelectionModel().getSelectedIndex()

class EzText(EzControl):
    def GetText(self): return self.ctrl.getText()
    def SetText(self,text): self.ctrl.setText(text)
    def Insert(self,index,text): self.ctrl.insertText(index,text)
    def Append(self,text): self.ctrl.appendText(text)
    def Clear(self): self.ctrl.clear()
    def Copy(self): self.ctrl.copy()
    def Paste(self): self.ctrl.paste()
        
class EzTextField(EzText):
    def __init__(self,h):
        from javafx.scene.control import TextField
        self.ctrl = TextField()
        self.ctrl.setText(h.get('text'))
        SetFileDropHandler( self.ctrl, self.DropHandler )
    def DropHandler(self,files):
        self.ctrl.setText( files.get(0).getPath() )
        
class EzTextArea(EzText):
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

def EzMenu(name,menu_table):
    from javafx.scene.control import Menu
    from javafx.scene.control import MenuBar
    from javafx.scene.control import MenuItem
    from javafx.scene.control import SeparatorMenuItem
    menu = Menu(name)
    for m in menu_table:
        if not m.get('name') or m['name'] == '-':
            menu.getItems().add(SeparatorMenuItem());
        if not m.get('item'): continue # Disabled
        if type(m['item']) == list:
            menu.getItems().add(EzMenu(m['name'],m['item']))
        else:
            item = MenuItem(m['name'])
            item.setOnAction(m['item'])
            menu.getItems().add(item);
    return menu

def EzMenuBar(menubar_table):
    from javafx.scene.control import Menu
    from javafx.scene.control import MenuBar
    from javafx.scene.control import MenuItem
    from javafx.scene.control import SeparatorMenuItem
    menubar = MenuBar()
  
    for m in menubar_table:
        if m.get('name'):
            menubar.getMenus().add(EzMenu(m['name'],m['item']))
        else:
            if m.get('fontsize'):
                menubar.setStyle("-fx-font: " + m['fontsize'] + " arial;")  
    return menubar

def EzToolBar(toolbar_table):
    from javafx.scene.control import Separator;
    from javafx.scene.control import ToolBar;
    from javafx.scene.control import Tooltip        
    toolbar = ToolBar();
    for h in toolbar_table:
        if not h.get('name') or h['name'] == '-':
            toolbar.getItems().add(Separator())
            continue
        name = h['name'] 
        if   name == 'Label': f = EzLabel(h)
        elif name == 'Button': f = EzButton(h)
        elif name == 'ToggleButton': f = EzToggleButton(h)
        elif name == 'TextField': f = EzTextField(h)
        elif name == 'ChoiceBox': f = EzChoiceBox(h)
        elif name == 'ComboBox': f = EzComboBox(h)
        else: continue  
        if h.get('width'): f.ctrl.setPrefSize(h['width'], -1);
        if h.get('fontsize'): f.ctrl.setStyle("-fx-font: " + h['fontsize'] + " arial;");                
        if h.get('tooltip'): f.ctrl.setTooltip(Tooltip(h['tooltip']))           
        if h.get('handler'): f.ctrl.setOnAction(h['handler'])
        if h.get('key'): __ctrl_table[h['key']] = f        
        toolbar.getItems().add(f.ctrl);
    return toolbar

def EzLayout(content):
    from javafx.scene.control import Tooltip
    vbox = EzVBox(1,1)
    for v in content:
        hbox = EzHBox(1,1)
        expand = False
        for h in v:
            name = h.get('name')
            if not name:
                if h.get('expand'): expand = h['expand']
                continue
            if   name == 'Label': f = EzLabel(h)
            elif name == 'Button': f = EzButton(h)
            elif name == 'ToggleButton': f = EzToggleButton(h)
            elif name == 'ChoiceBox': f = EzChoiceBox(h)
            elif name == 'ComboBox': f = EzComboBox(h)
            elif name == 'TextField': f = EzTextField(h)
            elif name == 'TextArea': f = EzTextArea(h)
            elif name == 'HSplit': f = EzHSplitPane(h)
            elif name == 'VSplit': f = EzVSplitPane(h)
            elif name == 'TabPane': f = EzTabPane(h)
            else: continue
            
            width = -1
            height = -1
            if h.get('width') : width  = h['width']
            if h.get('height'): height = h['height']
            f.ctrl.setPrefSize(width, height);

            if h.get('fontsize'): f.ctrl.setStyle("-fx-font: " + h['fontsize'] + " arial;");                
            if h.get('tooltip'): f.ctrl.setTooltip(Tooltip(h['tooltip']))
            if h.get('key'): __ctrl_table[h['key']] = f
            hbox.addItem(f.ctrl,expand=h.get('expand'))
        vbox.addItem(hbox.ctrl,expand=expand)
    return vbox.ctrl

def GetControl(name):
    return __ctrl_table.get(name)

class EzWindow(Application):
    def start(self, stage):
        from javafx.application import Platform
        self.ctrl  = __ctrl_table
        self.stage = stage
        self.stage.setTitle("FxApp Example")
        if self.closeHandler: self.stage.setOnCloseRequest(self.closeHandler)
        Platform.setImplicitExit(True)
        pane = EzBorderPane(self)
        self.scene = Scene(pane.ctrl, 640, 400)
        self.stage.setScene(self.scene)
        self.stage.show()
    def SetCloseHandler(self,handler): self.closeHandler = handler
    def Close(self): self.stage.close()
    def SetContent(self,content): self.content = content
    def Alert(self, title, message): EzAlert(title,message,self.stage)
    def YesNo(title, message, stage=None): return EzYesNo(title,message,self.stage)
    def FileOpenDialog(self, initialFile): return EzFileOpenDialog(initialFile, self.stage)
    def FileSaveDialog(self, initialFile): return EzFileSaveDialog(initialFile, self.stage)
    
    
#
# Application
#

      
class FxApp(EzWindow):
    def __init__(self):
        self.menu = [
            { 'name' : "File",
              'item' : [
                    { 'name' : "Exit" , 'item' : self.onExit, 'icon' : 'exit' },
                    { 'name' : "-" },
                    { 'name' : "About", 'item' : self.onAbout, 'icon' : 'help' } ]
            }, { 'name' : "Help",
              'item' : [
                    { 'name' : "About", 'item' : self.onAbout, 'icon' : 'help' } ]
            }]
        self.tool = [
                { "name" : "Label", "label" : "Address:" },
                { "name" : "ChoiceBox", "key" : "choice", 'handler' : self.onChoice, 'items' : ["apple","orange"] },
                { "name" : "ComboBox", "key" : "combo", 'handler' : self.onCombo, 'items' : ["apple","orange"] },
                { "name" : "TextField", "key" : "texttool", "width" : 100 },
                { "name" : "Button",  "label" : "Exit", "handler" : self.onExit, "tooltip" : "Quit", 'icon' : 'icon/exit.png'  },
                { "name" : "ToggleButton", "label" : "Toggle", "handler" : self.onToggle, "tooltip" : "Toggle", 'icon' : 'icon/open.png'  },
            ]
        tab1 = [[ { "name" : "TextArea", "expand" : True },
                  { "expand" : True }, ]]
        tab2 = [[ { "name" : "TextArea", "expand" : True },
                  { "expand" : True }, ]]
        split1 = [[
                { "name" : "TabPane", "labels" : [ "Tab1", "Tab2" ], "items" : [ tab2, tab2 ], "expand" : True },
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
                { "name" : "HSplit", "items" : [ split1, split2 ] , "first" : 0.5, "expand" : True},
                { "expand" : True },
            ],                
        ]
        self.SetCloseHandler(self.onClose)
    def onAbout(self,event):
        v = EzYesNo("Global", "Dialog")
        if v: self.Alert("Result", "Yes")
        else: self.Alert("Result", "No")
    def onBrowse(self,event):
        f = EzFileOpenDialog(None)
        ctrl = GetControl('text')
        if ctrl: ctrl.setText(f.getPath())
    def onExit(self,event):
        from javafx.application import Platform
        Platform.exit()
        #from java.lang import System
        #System.exit(0)
        #self.Close()
    def onClose(self,event):
        v = EzYesNo("Alert", "Do you want to quit ?", self.stage)
    def onChoice(self,newvalue):
        c = GetControl('choice')
        t = GetControl('texttool')
        t.SetText( c.GetSelectedItem() )
    def onCombo(self,newvalue):
        c = GetControl('combo')
        t = GetControl('texttool')
        t.SetText( c.GetSelectedItem() )
    def onToggle(self,newvalue):
        print("toggle")
        
if __name__ == '__main__':
    Application.launch(FxApp().class, [])
