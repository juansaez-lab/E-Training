import { Component, OnInit } from '@angular/core';
import { CardComponent } from 'src/app/shared/components/card-component/card-component';
import { menu } from './menu';
import { isUserInRole, userRole } from 'src/app/utils/user.utils';
import { MenuOption } from 'src/app/interfaces/menuOption';
import { CommonModule } from '@angular/common';
import {BaseComponent} from "../base.component";
import {BasePage} from "../base.page";
@Component({
  selector: 'app-home-component',
  imports: [CommonModule, CardComponent],
  templateUrl: './home-component.html',
  standalone: true,
  styleUrl: './home-component.css'
})
export class HomeComponent extends BasePage {

  options: MenuOption[] = [];

  ngOnInit(): void {
    super.ngOnInit();
    this.options =  menu.filter(m => isUserInRole(m.roles));
  }

  /*get options(): MenuOption[] {
    const userOptions = this.optionList.filter(m => isUserInRole(m.roles));
    return userOptions;
  }*/
}
