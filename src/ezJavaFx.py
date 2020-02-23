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

class FxSplitPane(object):
    def addItem(self,item): self.ctrl.getItems().add(item);
    def addItems(self,layout):
        items = layout.get('items')
        if items:
            for item in items:
                self.addItem(Layout(item).ctrl)
        
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
        self.ctrl = Button()
        self.ctrl.setText(h.get('label'))
        if h.get('handler'):
            self.ctrl.setOnAction( h['handler'] )

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
    
def Layout(content):
    print('Layout')
    vbox = FxVBox(5,5)
    for v in content:
        hbox = FxHBox(5,5)
        expand = False
        for h in v:
            name = h.get('name')
            if not name:
                if h.get('expand'): expand = h['expand']
                continue
            if   name == 'Label':     f = FxLabel(h)
            elif name == 'Button':    f = FxButton(h)
            elif name == 'TextField': f = FxTextField(h);
            elif name == 'TextArea':  f = FxTextArea(h);
            elif name == 'HSplit':    f = FxHSplitPane(h);
            elif name == 'VSplit':    f = FxVSplitPane(h);
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
    return vbox

def getCtrl(name):
    return __ctrl_table.get(name)
    
class Window(Application):
    def start(self, stage):
        self.ctrl  = __ctrl_table
        self.stage = stage
        self.stage.setTitle("FxApp Example")
        v = Layout(self.content)
        self.scene = Scene(v.ctrl, 640, 400)
        self.stage.setScene(self.scene)
        self.stage.show()
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
                { "name" : "HSplit", "first" : 0.5, "expand" : True,
                   "items" : [
                        [ #vbox
                            [ #hbox
                                { "name" : "TextArea", "expand" : True },
                                { "expand" : True },                        
                            ],
                        ],
                        [ #vbox
                            [ #hbox
                                { "name" : "TextArea", "expand" : True },
                                { "expand" : True },                        
                            ],
                        ],
                    ]
                },
                { "expand" : True },
            ],                
        ]

    def onAbout(self,event):
        v = YesNo("Global", "Dialog")
        if v: self.Alert("Result", "Yes")
        else: self.Alert("Result", "No")

    def onBrowse(self,event):
        f = FileOpenDialog(None)
        ctrl = getCtrl('text')
        if ctrl:                
            ctrl.setText(f.getPath())
 
if __name__ == '__main__':
    Application.launch(FxApp().class, [])
