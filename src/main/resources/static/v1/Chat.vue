<template>
    <div>
        <div>
            <label for="name">请输入用户名</label>
            <input type="text" id="name" v-model="userName" placeholder="用户名">
        </div>
        <div>
            <button id="connect" type="button" :disabled="connectDisable" @click="connect()">连接</button>
            <button id="disconnect" type="button" :disabled="!connectDisable" @click="disconnect()">断开连接</button>
        </div>
        <div id="chat" style="display: none;"></div>
        <div>
            <label for="name">请输入聊天内容：</label>
            <input type="text" id="content" v-model="content" placeholder="聊天内容">
        </div>
        <button id="send" type="button" @click="sendMessage()">发送</button>
        <hr>
        <div id="greeting" v-for="item in greetings">
            <div>{{item.name+'：'+item.content}}</div>
        </div>
    </div>
</template>

<script>
    import SockJS from 'sockjs-client';
    import Stomp from 'stompjs';

    export default {
        data() {
            return {
                stompClient: null,
                timer: '',
                userName: '',
                content: '',
                connectDisable: false,
                greetings: []
            }
        },
        methods: {
            setConnected() {
                this.connectDisable = !this.connectDisable
            },

            /**
             * websocket连接
             */
            connect() {
                const socket = new SockJS('http://localhost:8080/endpoint-websocket');
                const stompClient = Stomp.over(socket);
                this.stompClient = stompClient
                stompClient.connect({}, (frame) => {
                    this.setConnected()
                    console.log('Connected: ' + frame);
                    stompClient.subscribe('/topic/message', (result) => {
                        console.log(JSON.parse(result.body))
                        this.greetings.push(JSON.parse(result.body))
                    });
                });
            },
            /**
             * 发布消息
             */
            sendMessage() {
                this.stompClient.send("/app/v1/message", {}, JSON.stringify({
                    'name': this.userName,
                    'content': this.content
                }));

            },
            disconnect() {
                if (this.stompClient !== null) {
                    this.stompClient.disconnect();
                }
                this.setConnected()

            }, // 断开连接
        },
        mounted() {

        },
        beforeDestroy: function () {
            // 页面离开时断开连接,清除定时器
            this.disconnect();
            clearInterval(this.timer);
        }
    }
</script>

<style>
</style>
