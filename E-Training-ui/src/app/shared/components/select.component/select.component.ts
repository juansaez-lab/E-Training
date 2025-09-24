import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {Option} from "./option";

@Component({
    selector: 'app-select',
    templateUrl: './select.component.html',
    imports: [FormsModule, CommonModule],
    standalone: true,
    styleUrls: ['./select.component.css']
})
export class SelectComponent implements OnInit, OnChanges {
    @Input() label: string = '';
    @Input() value: string;
    @Input() items: Option[];
    @Input() class: string;
    @Input() style: string;


    valueInternal: string = '';
    focused: boolean = false;

    @Output() onchange=  new EventEmitter<any>();

    ngOnInit(): void {
        this.valueInternal = this.value;
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.ngOnInit();
    }

    changevalue($event) {
        this.valueInternal = $event.target.value;
        this.onchange.emit(this.valueInternal)
    }
}
