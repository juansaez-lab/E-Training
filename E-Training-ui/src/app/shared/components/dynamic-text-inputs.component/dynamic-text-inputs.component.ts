import {Component, EventEmitter, Input, Output, OnInit, OnChanges, SimpleChanges} from '@angular/core';
import {CommonModule} from "@angular/common";

interface InputField {
    index: number;     // número de la marca {{n}}
    value: string;     // valor actual del input
}

@Component({
    selector: 'app-dynamic-text-inputs',
    imports: [CommonModule],
    templateUrl: './dynamic-text-inputs.component.html',
    standalone: true,
    styleUrls: ['./dynamic-text-inputs.component.css']
})
export class DynamicTextInputsComponent implements OnInit, OnChanges {
    @Input() textWithMarks: string = ''; // Texto con marcas {{1}}, {{2}}, ...
    @Input() initialValues: { [key: number]: string } = {}; // Valores iniciales para cada input

    @Output() valueChanged = new EventEmitter<{ index: number, value: string }>();

    // Array de fragmentos que pueden ser texto o input
    //parsedParts: Array<{ type: 'text' | 'input', content: string, index?: number }> = [];
    parsedParts: any[];

    ngOnInit() {
        this.parseText();
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.ngOnInit();
    }

    private parseText() {
        const regex = /{{(\d+)}}/g;
        let lastIndex = 0;
        let match;

        this.parsedParts = [];

        while ((match = regex.exec(this.textWithMarks)) !== null) {
            const indexNum = Number(match[1]);

            // Texto antes de la marca
            if (match.index > lastIndex) {
                this.parsedParts.push({
                    type: 'text',
                    content: this.textWithMarks.substring(lastIndex, match.index)
                });
            }

            // Input en la posición de la marca
            this.parsedParts.push({
                type: 'input',
                content: this.initialValues[indexNum] ?? '',
                index: indexNum
            });

            lastIndex = match.index + match[0].length;
        }

        // Texto después de la última marca
        if (lastIndex < this.textWithMarks.length) {
            this.parsedParts.push({
                type: 'text',
                content: this.textWithMarks.substring(lastIndex)
            });
        }
    }

    get parts() {
        return this.parsedParts;
    }

    onInputChange(index: number, value: string) {
        this.valueChanged.emit({ index, value });
    }
}
