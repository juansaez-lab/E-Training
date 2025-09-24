import { CommonModule } from '@angular/common';
import {Component, inject, Input, OnInit} from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { getUser } from 'src/app/core/services/utils.service';

@Component({
  selector: 'page-header',
  imports: [RouterModule, CommonModule],
  templateUrl: './page-header.html',
  standalone: true,
  styleUrl: './page-header.css'
})
export class PageHeader implements OnInit {
  @Input() key: string;
  user: string = '';

  router: Router = inject(Router);

  ngOnInit(): void {
    const user = getUser();
    this.user = user !== undefined && user !== null? user.name + ' ' + user.surname + ' as ' + user.role: '';
  }
  title: string = "e-Training";

  onLogout() {
    if (confirm("Está seguro de cerrar sessión")) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
  }
}
