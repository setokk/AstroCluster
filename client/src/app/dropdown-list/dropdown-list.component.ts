import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-dropdown-list',
  standalone: true,
  imports: [],
  templateUrl: './dropdown-list.component.html',
  styleUrl: './dropdown-list.component.css'
})
export class DropdownListComponent {
  @Input() options: { label: string, value: string }[] = [];
  @Input() placeholder: string = 'Select an option';
  @Input() name: string = '';
  @Input() label: string = '';

  @Output() selectionChange = new EventEmitter<any>();

  isOpen = false;
  selectedOption: { label: string, value: string } | null = null;

  onToggleDropdown() {
    this.isOpen = !this.isOpen;
  }

  onSelectOption(option: { label: string, value: string }) {
    this.selectedOption = option;
    this.selectionChange.emit(option.value);
  }
}
