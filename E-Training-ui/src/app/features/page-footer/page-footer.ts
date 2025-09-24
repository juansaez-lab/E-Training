import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'page-footer',
  imports: [],
  templateUrl: './page-footer.html',
  standalone: true,
  styleUrl: './page-footer.css'
})
export class PageFooter implements OnInit {
  @Input() key: string;
  ngOnInit(): void {
  }

}
