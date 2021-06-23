import React, {useState} from 'react';
import {Button, Form, Input, Space} from "antd";
import axios from "axios";
import URL from '../URL';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './index.css';

const USERNAME_RULES = [
    {
        required: true,
        message: 'URL is required!',
    },
]


const initialValues = {
    url: 'ws://localhost:8080/streaming'
}

let websocket = null;

const ConnectToDevice = () => {
    const [list, setList] = useState([]);

    const [form] = Form.useForm()

    const handleSubmit = async (values) => {
        const {data} = await axios.get(URL.CONNECT, {
            params: {
                link: values.url,
                operation: 'SUM'
            },
            withCredentials: true
        });

        localStorage.setItem('data', data);
        console.log(data);


        const socket = new SockJS('/streaming')
        websocket = Stomp.over(socket);
        websocket.connect({}, function (frame) {
            websocket.subscribe('/topic/greetings', function (greeting) {
                list.push(greeting.body)

                setList([...list].reverse())
                console.log(list);
            });
        });
    }

    const onClose = async () => {
        await axios.get(URL.CLOSE, {
            withCredentials: true
        });

        websocket.disconnect();
        setList([]);
    }

    return (
        <div>
            <Form form={form} initialValues={initialValues} onFinish={handleSubmit}>
                <Form.Item name="url" rules={USERNAME_RULES}>
                    <Input placeholder="Url to Device"/>
                </Form.Item>


                <Space>
                    <Button type="primary" htmlType="submit">
                        Connect
                    </Button>
                    {/*<Button type="primary" onClick={onClose}>*/}
                    {/*    Close*/}
                    {/*</Button>*/}
                </Space>
            </Form>
            <div className={'track'}>
                {
                    list.map(e=>{
                        return <p>{e}</p>
                    })
                }
            </div>
        </div>
    );
};

export default ConnectToDevice;