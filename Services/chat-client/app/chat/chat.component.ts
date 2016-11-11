import { Component, OnInit } from '@angular/core';
import { ChatService, Message } from './../services/chat/chat.service';

@Component({
  moduleId: module.id,
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  private messages: Message[] = [];

  constructor(private chatService: ChatService) {
  }

  ngOnInit() {
    this.chatService.messages.subscribe(msg => {
      this.messages.push(msg);
    });
  }
}