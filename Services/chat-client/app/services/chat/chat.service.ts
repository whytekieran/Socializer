import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import {WebSocketService } from './websocket.service';

const CHAT_URL = 'ws://localhost:8081';

export interface Message {
	//Socket message base
    version: number,
	token: string,
	state: number,
    status?: number,
    
    //Error message
    src?: string,
    code?: number,
    desc?: string,
    
    //Action Message
    action?: number,
    action_values?: string[],
    result?: string,

    //Session message
    skey?: string,
    session?: string,
    msgbody?: string,
    msgid?: string
}

@Injectable()
export class ChatService {
	public messages: Subject<Message>;

	constructor(wsService: WebSocketService) {
		this.messages = <Subject<Message>>wsService
			.connect(CHAT_URL)
			.map((response: MessageEvent): Message => {
				let data = JSON.parse(response.data);
				return data;
			});
	}
}