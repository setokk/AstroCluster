export class DateUtils {
    static formatDateString(input: string): string {
        const date = new Date(input);
        const day = date.getDate();
        const month = date.toLocaleString('default', { month: 'long' });
        const year = date.getFullYear();

        function getDaySuffix(n: number): string {
            if (n >= 11 && n <= 13) return 'th';
            switch (n % 10) {
                case 1: return 'st';
                case 2: return 'nd';
                case 3: return 'rd';
                default: return 'th';
            }
        }

        const hour = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        return `${month} ${day}${getDaySuffix(day)} ${year}, ${hour}:${minutes}`;
    }

}