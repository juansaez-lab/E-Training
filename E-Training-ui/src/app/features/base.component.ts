import {Directive, Input, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import { isUserInRole } from "../utils/user.utils";


@Directive()
export abstract class BaseComponent implements OnInit, OnChanges {
    @Input() ifUserInRole: string[] = [];

    show: boolean = true;

    ngOnInit(): void {    
        this.show = this.mustShow()
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.ngOnInit()
    }

    mustShow() : boolean {
        return isUserInRole(this.ifUserInRole);
    }


}
