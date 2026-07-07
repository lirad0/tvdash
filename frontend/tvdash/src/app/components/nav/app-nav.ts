import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';

@Component({
	selector: 'app-nav',
	templateUrl: './app-nav.html',
	imports: [CommonModule, FormsModule, ButtonModule]
})
export class AppNav {
	visible = false;
	name = '';
	url = '';
	imageDataUrl: string | null = null;
	imageFile: File | null = null;

	onFileChange(event: Event) {
		const input = event.target as HTMLInputElement;
		if (input.files && input.files[0]) {
			this.imageFile = input.files[0];
			const reader = new FileReader();
			reader.onload = () => {
				this.imageDataUrl = reader.result as string;
			};
			reader.readAsDataURL(this.imageFile);
		}
	}

	save() {
		// implement saving behavior as needed; currently closes the sidebar
		this.visible = false;
	}

	open() { this.visible = true; }
	close() { this.visible = false; }
}

