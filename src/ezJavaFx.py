from javafx.application import Application
from javafx.scene import Scene
from javafx.scene import Node
from javafx.scene.layout import VBox
from javafx.scene.layout import HBox
from javafx.scene.layout import Priority
from javafx.geometry import Insets
from javafx.geometry import Orientation
from javafx.geometry import Pos

__ctrl_table = {}

#
# Container
#

class FxBox():
    def getItem(self,index):
        return self.ctrl.getChildren().get(index)
    def addItem(self,item,expand=False): #Node
        self.ctrl.getChildren().add(item)
        if expand == True:
            self.setExpand(item)
    def alignLeft(self):
        self.setAlignLeft()
    def alignRight(self):
        self.setAlignRight()
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
    def getText(self):
        return self.ctrl.getText()
    def setText(self,text):
        self.ctrl.setText(text)
    def insert(self,index,text):
        self.ctrl.insertText(index,text)
    def append(self,text):
        self.ctrl.appendText(text)
    def clear(self):
        self.ctrl.clear()
    def copy(self):
        self.ctrl.copy()
    def paste(self):
        self.ctrl.paste()
        
class FxTextField(FxText):
    def __init__(self,h):
        from javafx.scene.control import TextField
        self.ctrl = TextField()
        self.ctrl.setText(h.get('text'))

class FxTextArea(FxText):
    def __init__(self,h):
        from javafx.scene.control import TextArea
        self.ctrl = TextArea()
        self.ctrl.setText(h.get('text'))

#
# Window
#

def Layout(content):
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

    def SetContent(self,content):
        self.content = content
        

#
# Application
#

def onButton(event):
    ctrl = getCtrl('text')
    if ctrl:
        ctrl.setText("1111")
            
class FxApp(Window):
    def __init__(self):
        self.content = [ # vbox
            [ # hbox
                { "name" : "Label", "label" : "Address:" },
                { "name" : "TextField", "key" : "text", "expand" : True },
                { "name" : "Button",  "label" : "Browse", "tooltip" : "Open File", "handler" : onButton  },
            ],
            [ # hbox
                { "name" : "TextArea", "expand" : True },
                { "expand" : True },
            ],    
        ]

if __name__ == '__main__':
    Application.launch(FxApp().class, [])
