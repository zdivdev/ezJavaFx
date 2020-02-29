from javafx.application import Application
from javafx.scene import Scene
from javafx.scene import Node
from javafx.scene.layout import VBox
from javafx.scene.layout import HBox
from javafx.scene.layout import Priority
from javafx.scene.input import Clipboard
from javafx.scene.input import ClipboardContent 
from javafx.geometry import Insets
from javafx.geometry import Insets
from javafx.geometry import Orientation
from javafx.geometry import Pos
from javafx.embed.swing import SwingFXUtils

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
        for m in w.tool:
            v.addItem(EzToolBar(m))
        self.setTop(v.ctrl)
        self.setBottom(EzStatusBar(w.status))
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
        tab.setClosable(False)
        tab.setContent(item)
        self.ctrl.getTabs().add(tab)
              
#
# Control
#

class EzControl():
    def Initialize(self,h):
        from javafx.scene.control import Tooltip
        if h.get('width') or h.get('height'):
            width = -1
            height = -1
            if h.get('width') : width  = h['width']
            if h.get('height'): height = h['height']
            self.ctrl.setPrefSize(width, height);
        if h.get('tooltip'): self.ctrl.setTooltip(Tooltip(h['tooltip']))           
        if h.get('menu'): self.ctrl.setContextMenu(EzContextMenu(h['menu']))
        if h.get('key'): __ctrl_table[h['key']] = self
        if h.get('fontsize'): self.SetFontSize(h['fontsize'])                
        if h.get('icon'): self.SetIcon( h['icon'], h.get('icon_top') )
            
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
        if top: self.ctrl.setContentDisplay(ContentDisplay.TOP)

class EzLabel(EzControl):
    def __init__(self,h):
        from javafx.scene.control import Label
        self.ctrl = Label()
        self.Initialize(h)
        self.ctrl.setText(h.get('label'))
        if h.get('handler'): self.ctrl.setOnAction(h['handler'])
        #ctrl.setAlignment(Pos.CENTER);
        
class EzButton(EzControl):
    def __init__(self,h):   
        from javafx.scene.control import Button
        self.ctrl = Button()
        self.Initialize(h)
        self.ctrl.setText(h.get('label'))
        if h.get('handler'): self.ctrl.setOnAction( h['handler'] )
        
class EzToggleButton(EzControl):
    def __init__(self,h):
        from javafx.scene.control import ToggleButton
        self.ctrl = ToggleButton()
        self.Initialize(h)
        self.ctrl.setText(h.get('label'))
        if h.get('handler'): self.ctrl.setOnAction( h['handler'] )
    def IsSelected(self):
        return self.ctrl.isSelected()
    def SetSelected(self,v):
        return self.ctrl.setSelected(v)
        
class EzCheckBox(EzControl):
    def __init__(self,h):
        from javafx.scene.control import CheckBox
        self.ctrl = CheckBox()
        self.Initialize(h)
        self.ctrl.setText(h.get('label'))
        if h.get('handler'): self.ctrl.setOnAction( h['handler'] )
    def IsSelected(self):
        return self.ctrl.isSelected()
    def SetSelected(self,v):
        return self.ctrl.setSelected(v)
        
class EzChoiceBox(EzControl):
    def __init__(self,h):
        from javafx.beans.value import ChangeListener
        from javafx.beans.value import ObservableValue
        from javafx.scene.control import ChoiceBox
        self.ctrl = ChoiceBox()
        self.Initialize(h)
        if h.get('handler'): self.ctrl.setOnAction(h['handler'])
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
        self.Initialize(h)
        if h.get('handler'): self.ctrl.setOnAction(h['handler'])
        if h.get('items'): self.ctrl.getItems().addAll( h['items'] )
        self.ctrl.getSelectionModel().selectFirst()    
    def Add(self,item):
        self.ctrl.getItems().add(item)
    def GetSelectedItem(self):
        return self.ctrl.getSelectionModel().getSelectedItem()
    def GetSelectedIndex(self):
        return self.ctrl.getSelectionModel().getSelectedIndex()

class EzListView(EzControl):
    def __init__(self,h):
        from javafx.beans.value import ChangeListener
        from javafx.beans.value import ObservableValue
        from javafx.scene.control import ListView
        self.ctrl = ListView()
        self.Initialize(h)
        if h.get('handler'): self.ctrl.getSelectionModel().selectedItemProperty().addListener(h['handler'])
        if h.get('items'): self.ctrl.getItems().addAll( h['items'] )
        self.ctrl.getSelectionModel().selectFirst() 
    def Add(self,item):
        self.ctrl.getItems().add(item)
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
        self.Initialize(h)
        self.ctrl.setText(h.get('text'))
        if h.get('handler'): self.ctrl.setOnAction(h['handler'])
        SetFileDropHandler( self.ctrl, self.DropHandler )  
    def DropHandler(self,files):
        self.ctrl.setText( files.get(0).getPath() )
        
class EzTextArea(EzText):
    def __init__(self,h):
        from javafx.scene.control import TextArea
        self.ctrl = TextArea()
        self.Initialize(h)
        if h.get('handler'): self.ctrl.setOnAction(h['handler'])
        self.ctrl.setText(h.get('text'))

class EzProgressBar(EzControl):
    def __init__(self,h):   
        from javafx.scene.control import ProgressBar
        self.ctrl = ProgressBar()
        self.Initialize(h)
        if h.get('handler'): self.ctrl.setOnAction(h['handler'])
    def GetValue(self):
        return self.ctrl.getProgress()
    def SetValue(self,v):
        self.ctrl.setProgress(v)

#
# Window
#

def RunLater(handler):
    from javafx.application import Platform
    Platform.runLater(handler)

def DragOver(event):
    from javafx.scene.input import TransferMode
    if event.getDragboard().hasFiles():
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE)
    event.consume()

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
    from javafx.scene.control import ContextMenu
    from javafx.scene.control import MenuItem
    from javafx.scene.control import SeparatorMenuItem
    if not name: menu = ContextMenu()
    else: menu = Menu(name)
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

def EzContextMenu(menu_table):
    return EzMenu(None,menu_table)
    
def EzMenuBar(menubar_table):
    from javafx.scene.control import MenuBar
    menubar = MenuBar()
    for m in menubar_table:
        if m.get('name'):
            menubar.getMenus().add(EzMenu(m['name'],m['item']))
        else:
            if m.get('fontsize'):
                menubar.setStyle("-fx-font: " + m['fontsize'] + " arial;")  
    return menubar

def EzToolBar(toolbar_table):
    from javafx.scene.control import Separator
    from javafx.scene.control import ToolBar      
    ctrl = ToolBar();
    for h in toolbar_table:
        if not h.get('name') or h['name'] == '-':
            ctrl.getItems().add(Separator())
            continue
        name = h['name'] 
        if   name == 'Label': f = EzLabel(h)
        elif name == 'Button': f = EzButton(h)
        elif name == 'CheckBox': f = EzCheckBox(h)
        elif name == 'ChoiceBox': f = EzChoiceBox(h)
        elif name == 'ComboBox': f = EzComboBox(h)
        elif name == 'ProgressBar': f = EzProgressBar(h)
        elif name == 'ToggleButton': f = EzToggleButton(h)
        elif name == 'TextField': f = EzTextField(h)
        else: continue  
        ctrl.getItems().add(f.ctrl);
    return ctrl

def EzStatusBar(statusbar_table):
    from javafx.scene.control import Separator     
    from javafx.scene.layout import Region     
    hbox = EzHBox(1,1);
    for h in statusbar_table:
        if not h.get('name'): continue
        if h['name'] == '--' or h['name'] == '--': hbox.addItem(Separator()); continue
        if h['name'] == '<>':
            space = Region(); HBox.setHgrow(space, Priority.ALWAYS)
            hbox.addItem(space)
            continue
        name = h['name'] 
        if   name == 'Label': f = EzLabel(h)
        elif name == 'Button': f = EzButton(h)
        elif name == 'CheckBox': f = EzCheckBox(h)
        elif name == 'ChoiceBox': f = EzChoiceBox(h)
        elif name == 'ComboBox': f = EzComboBox(h)
        elif name == 'ProgressBar': f = EzProgressBar(h)
        elif name == 'ToggleButton': f = EzToggleButton(h)
        elif name == 'TextField': f = EzTextField(h)
        else: continue  
        hbox.addItem(f.ctrl);
    return hbox.ctrl

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
            elif name == 'CheckBox': f = EzCheckBox(h)
            elif name == 'ChoiceBox': f = EzChoiceBox(h)
            elif name == 'ComboBox': f = EzComboBox(h)
            elif name == 'ListBox': f = EzListView(h)
            elif name == 'ProgressBar': f = EzProgressBar(h)
            elif name == 'TextField': f = EzTextField(h)
            elif name == 'TextArea': f = EzTextArea(h)
            elif name == 'ToggleButton': f = EzToggleButton(h)
            elif name == 'TabPane': f = EzTabPane(h)
            elif name == 'HSplit': f = EzHSplitPane(h)
            elif name == 'VSplit': f = EzVSplitPane(h)
            else: continue
            hbox.addItem(f.ctrl,expand=h.get('expand'))
        vbox.addItem(hbox.ctrl,expand=expand)
    return vbox.ctrl

def GetControl(name):
    return __ctrl_table.get(name)

def ClipboardClear():
    Clipboard.getSystemClipboard().clear();
def GetClipboardText():
    if Clipboard.getSystemClipboard().hasString():
        return Clipboard.getSystemClipboard().getString()
def GetClipboardHtmlText():
    if Clipboard.getSystemClipboard().hasHtml():
        return Clipboard.getSystemClipboard().getHtml()
def GetClipboardFiles():
    if Clipboard.getSystemClipboard().hasFiles():
        return Clipboard.getSystemClipboard().getFiles()
def GetClipboardImage():
    return SwingFXUtils.fromFXImage( Clipboard.getSystemClipboard().getImage()  )

def putClipboardText(text):
    content = ClipboardContent()
    content.putString(text);
    Clipboard.getSystemClipboard().setContent(content)
def putClipboardHtmlText(text):
    content = ClipboardContent()
    content.putString(text)
    content.putHtml(text)
    Clipboard.getSystemClipboard().setContent(content)
def putClipboardFiles(files):
    content = ClipboardContent()
    content.putFiles(files)
    Clipboard.getSystemClipboard().setContent(content)
def putClipboardImage(image):
    content = ClipboardContent()
    content.putImage(SwingFXUtils.toFXImage(image, null))
    Clipboard.getSystemClipboard().setContent(content)

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
        if self.createdHandler: self.createdHandler()
        self.stage.show()
    def SetCloseHandler(self,handler): self.closeHandler = handler
    def SetCreatedHandler(self,handler): self.createdHandler = handler
    def Close(self): self.stage.close()
    def SetContent(self,content): self.content = content
    def Alert(self, title, message): EzAlert(title,message,self.stage)
    def YesNo(title, message, stage=None): return EzYesNo(title,message,self.stage)
    def FileOpenDialog(self, initialFile): return EzFileOpenDialog(initialFile, self.stage)
    def FileSaveDialog(self, initialFile): return EzFileSaveDialog(initialFile, self.stage)
  
#
# Application
#

def StartThread(handler,args):
    import threading
    thread = threading.Thread(target=handler,args=args)
    thread.daemon = True
    thread.start()
    
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
        self.tool = [[
                { "name" : "Label", "label" : "Address:", "menu" : self.menu },
                { "name" : "ChoiceBox", "key" : "choice", 'handler' : self.onChoice, 'items' : ["apple","orange"] },
                { "name" : "ComboBox", "key" : "combo", 'handler' : self.onCombo, 'items' : ["apple","orange"] },
                { "name" : "TextField", "key" : "texttool", "width" : 100 },
                { "name" : "ToggleButton", "label" : "Toggle", "handler" : self.onToggle, "tooltip" : "Toggle", 'icon' : 'icon/open.png'  },
                { "name" : "CheckBox", "label" : "Check", "handler" : self.onCheck, "tooltip" : "Check", 'icon' : 'icon/open.png'  },
            ],[
                { "name" : "Button",  "label" : "Exit", "handler" : self.onExit, "tooltip" : "Quit", 'icon' : 'icon/exit.png', 'icon_top' : True  },
            ]]
        self.status = [
                { "name" : "ProgressBar", 'key' : 'progress' },
                { "name" : "<>"},
            ]
        tab1 = [[ { "name" : "TextArea", "expand" : True },
                  { "expand" : True }, ]]
        tab2 = [[ { "name" : "ListBox", "key" : "listbox", 'handler' : self.onListBox, 'items' : ["apple","orange"], 'expand' : True },
                  { "expand" : True }, ]]
        split1 = [[
                { "name" : "TabPane", "labels" : [ "Tab1", "Tab2" ], "items" : [ tab1, tab2 ], "expand" : True },
                { "expand" : True }, ]]
        split2 = [[ { "name" : "TextArea", "expand" : True },
                    { "expand" : True }, ]] 
        self.content = [ # vbox
            [ # hbox
                { "name" : "Label", "label" : "Address:", "menu" : self.menu },
                { "name" : "TextField", "key" : "text", "expand" : True, "menu" : self.menu },
                { "name" : "Button",  "label" : "Browse", "tooltip" : "Open File", "handler" : self.onBrowse  },
                { "name" : "Button",  "label" : "About", "handler" : self.onAbout, "menu" : self.menu  },
            ],  
            [ # hbox
                { "name" : "HSplit", "items" : [ split1, split2 ] , "first" : 0.5, "expand" : True},
                { "expand" : True },
            ],                
        ]
        self.SetCloseHandler(self.onClose)
        self.SetCreatedHandler(self.created)

    def onAbout(self,event):
        v = EzYesNo("Global", "Dialog")
        if v: self.Alert("Result", "Yes")
        else: self.Alert("Result", "No")
    def onBrowse(self,event):
        f = EzFileOpenDialog(None)
        ctrl = GetControl('text')
        if ctrl and f: ctrl.SetText(f.getPath())
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
        if c and t: t.SetText( c.GetSelectedItem() )
    def onCombo(self,newvalue):
        c = GetControl('combo')
        t = GetControl('texttool')
        if c and t: t.SetText( c.GetSelectedItem() )
    def onListBox(self,newvalue):
        c = GetControl('listbox')
        t = GetControl('texttool')
        if c and t: t.SetText( c.GetSelectedItem() )
    def onToggle(self,newvalue):
        print("toggle")
    def onCheck(self,newvalue):
        print("toggle")
    def threadHandler(args):
        import time
        p = GetControl('progress')
        for i in range(100):
            RunLater(lambda : p.SetValue(1.0*i/100))
            time.sleep(0.1)
    def created(self):
        StartThread(self.threadHandler,None)
        DumpControlTable()      
        
if __name__ == '__main__':
    Application.launch(FxApp().class, [])
