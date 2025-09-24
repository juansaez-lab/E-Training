import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import {BaseComponent} from "../../../features/base.component";

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './button-component.html',
  styleUrl: './button-component.css'
})
export class ButtonComponent extends BaseComponent {
  @Input() text: string;
  @Input() clazz: string;
  @Input() type: ButtonType= ButtonType.PRIMARY;  
  @Input() size: ButtonSize= ButtonSize.LARGE;

  @Output() click = new EventEmitter<any>()

  styleClass: string = '';
  styleStyle: string = '';

  ngOnInit(): void {
    super.ngOnInit()
    let classToApply = (this.clazz? this.clazz: '') +  (this.type === ButtonType.PRIMARY ? " button_primary":" button_secondary");
    if (this.size !== ButtonSize.LARGE) {
      classToApply = classToApply + " " + (this.size == ButtonSize.MEDIUM? 'size_medium': 'size_small')
    }
    this.styleClass = classToApply
  }

  onClick() {    
    this.click.emit(this);
  }

}

export enum ButtonType {
  PRIMARY,
  SECONDARY
}

export enum ButtonSize {
  LARGE = "LARGE",
  MEDIUM = "MEDIUM",
  SMALL = "SMALL"
}
