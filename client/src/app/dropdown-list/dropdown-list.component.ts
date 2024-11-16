import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dropdown-list',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './dropdown-list.component.html',
  styleUrl: './dropdown-list.component.css'
})
export class DropdownListComponent {
  @Input() options: { key: string, value: string }[] = [];
  @Input() placeholder: string = 'Select an option';
  @Input() name: string = ''; // Input element name
  @Input() label: string = '';
  @Output() valueChange = new EventEmitter<string>();

  isOpen = false;
  selectedOptionKey: string = '';
  
  formData: { [key: string]: any } = {}; // Container for dynamic fields

  onToggleDropdown() {
    this.isOpen = !this.isOpen;
  }

  onSelectOption(option: { key: string, value: any }) {
    this.selectedOptionKey = option.key;
    this.valueChange.emit(option.key);
    this.formData[this.name] = option.value;
  }
}
