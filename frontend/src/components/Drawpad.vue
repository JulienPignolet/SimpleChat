<template>
  <v-dialog v-model="dialog" max-width="1200px">
    <v-card>
      <v-card-title>
        <span class="headline">Drawpad</span>
      </v-card-title>

      <v-card-text>
        <div class="d-flex">
          <div>
            <v-list>
              <v-subheader>FORMES</v-subheader>
              <v-list-item-group>
                <v-list-item>
                  <v-list-item-content v-on:click="setShape('rect')">
                    <v-icon x-large>mdi-crop-square</v-icon>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content v-on:click="setShape('circle')">
                    <v-icon x-large>mdi-checkbox-blank-circle-outline</v-icon>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content v-on:click="setShape('line')">
                    <v-icon x-large>mdi-chart-timeline-variant</v-icon>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>

            <v-list>
              <v-subheader>OUTILS</v-subheader>
              <v-list-item-group>
                <v-list-item>
                  <v-list-item-content v-on:click="resetCanvas()">
                    <v-icon x-large>mdi-trash-can-outline</v-icon>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content class="color-picker-container">
                    <v-icon
                      :style="colorSquare"
                      x-large
                      v-on:click="toggleColorPicker">
                      mdi-square
                    </v-icon>
                    <div class="color-picker" v-if="showColorPicker">
                      <v-color-picker v-model="color" />
                    </div>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </div>
          <canvas ref="my-canvas" width="1000" height="448" v-on:mousedown="mouseDown" v-on:mouseup="mouseUp" v-on:mousemove="mouseMove" v-on:mouseleave="mouseLeave">

          </canvas>
          <div class="hidden">
            <div v-for="(item, index) in items" :key="index">
              {{ item.message }}
            </div>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
  import * as types from "@/store/types.js";
  import store from '../store/index';
  import {sync} from "vuex-pathify";

  export default {
    name: 'drawpad',
    components: {},
    data () {
      return {
        showColorPicker: false,
        color: '#ff0000',
        shape: 'rect',
        shapesHistory: [],
        mouseIsDown: false,
        mouseDownClickPos: {x: 0, y: 0},
        mouseUpClickPos: {x: 0, y: 0},
        canvas: null,
        context: null,
        messageCount: 0
      }
    },
    updated() {
      try {
        this.canvas = this.$refs['my-canvas'];
        this.context = this.canvas.getContext('2d');
      } catch(error) {
        // Ne pas mettre un console.log pour afficher une erreur
        // console.log('Le canvas n\'est pas encore chargé');
      }

    },
    computed: {
      colorSquare: function() {
        return {
          color: `${this.color}`
        }
      },

      items: function() {
        let messages = store.state.chat.messageList.filter(message => message.type === 'drawpad');
        if(this.canvas !== null && this.canvas !== undefined && this.messageCount !== messages.length) {
          this.receiveMessage(messages);
        }
        return messages;
      },

      dialog: sync("interfaceControl/drawpad")
    },

    methods: {
      toggleColorPicker: function() {
        this.showColorPicker = !this.showColorPicker;
      },

      setShape: function(shape) {
        this.shape = shape;
      },

      mouseDown: function(event) {
        this.mouseDownClickPos = this.getMousePosition(event, this.canvas.getBoundingClientRect());
        this.mouseIsDown = true;
      },

      mouseUp: function(event) {
        this.mouseUpClickPos = this.getMousePosition(event, this.canvas.getBoundingClientRect());
        this.mouseIsDown = false;
        this.saveCurrentForm();
        this.sendMessage();
      },

      mouseLeave: function() {
        this.mouseIsDown = false;
      },

      mouseMove: function(event) {
        if(this.mouseIsDown && this.showColorPicker === false) {
          this.mouseUpClickPos = this.getMousePosition(event, this.canvas.getBoundingClientRect());
          this.draw();
        }
      },

      draw: function() {
        let dimension = {};
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.shapesHistory.forEach((shape) => {
          this.context.fillStyle = shape.color;
          this.context.strokeStyle = shape.color;
          dimension = this.getDimension(shape.startClick, shape.endClick);
          this.drawShape(shape.shape, shape.startClick.x,shape.startClick.y, dimension.width,dimension.height);
        });

        this.context.fillStyle = this.color;
        this.context.strokeStyle = this.color;
        dimension = this.getDimension(this.mouseDownClickPos, this.mouseUpClickPos);
        this.drawShape(this.shape,this.mouseDownClickPos.x,this.mouseDownClickPos.y,dimension.width,dimension.height);
      },

      drawShape: function(shape, x, y, width, height) {
        switch(shape) {
          case 'rect':
            this.context.fillRect(x,y,width,height);
            break;
          case 'circle':
            width = width > 0 ? width : -width;
            this.context.beginPath();
            this.context.arc(x ,y,width,0,2*Math.PI, false);
            this.context.fill();
            break;
          case 'line':
            this.context.beginPath();
            this.context.moveTo(x,y);
            this.context.lineTo(x + width, y + height);
            this.context.stroke();
            break;
        }
      },

      resetCanvas: function() {
        this.shapesHistory = [];
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
      },

      saveCurrentForm: function() {
        this.shapesHistory.push({
          color: this.color,
          shape: this.shape,
          startClick: this.mouseDownClickPos,
          endClick: this.mouseUpClickPos
        });
      },

      getMousePosition: function(event, rect) {
        return {
          x: event.clientX - rect.left,
          y: event.clientY - rect.top
        };
      },

      getDimension: function(startClick, endClick) {
        return {
          width: endClick.x - startClick.x,
          height: endClick.y - startClick.y
        }
      },

      sendMessage() {
        const shape = this.shapesHistory[this.shapesHistory.length - 1];
        console.log('message send :');
        console.log(shape);
        this.$store.dispatch(`chat/${types.sendMessage}`, {
          message: `${shape.color}|${shape.shape}|${shape.startClick.x}|${shape.startClick.y}|${shape.endClick.x}|${shape.endClick.y}`,
          type: 'drawpad'
        });
      },

      parseMessage(message) {
        const shape = message.split('|');
        this.shapesHistory.push({
          color: shape[0],
          shape: shape[1],
          startClick: {x: shape[2], y: shape[3]},
          endClick: {x: shape[4], y: shape[5]}
        });
      },

      receiveMessage(messages) {
        console.log('receive :');
        console.log(messages[messages.length - 1]);
        this.messageCount = messages.length;
        this.resetCanvas();
        messages.forEach(message => {
          this.parseMessage(message.message);
        });
        this.draw();
      }
    }
  }
</script>

<style lang="css">
  .hidden {
    display: none;
  }
  .color-picker-container {
    position: relative;
    overflow: visible;
  }

  .color-picker {
    position: absolute;
    bottom: 0;
    left: 100%;
  }

  canvas {
    /*flex: 1;*/
    cursor: crosshair;
    border: 1px solid rgba(0,0,0,0.1);
    margin-left: 24px;
    max-height: 448px;
  }
</style>
