<?php

namespace App\Console;

use Illuminate\Console\Scheduling\Schedule;
use App\Console\Commands\ChangeDriversToTrips;
use App\Console\Commands\OfflineUnAvailableDrivers;
use App\Console\Commands\NotifyDriverDocumentExpiry;
use App\Console\Commands\AssignDriversForScheduledRides;
use App\Console\Commands\AssignDriversForRegularRides;
use Illuminate\Foundation\Console\Kernel as ConsoleKernel;
use App\Console\Commands\ClearDemoDatabase;
use App\Console\Commands\ClearRequestTable;


class Kernel extends ConsoleKernel
{
    /**
     * The Artisan commands provided by your application.
     *
     * @var array
     */
    protected $commands = [
        ChangeDriversToTrips::class,
        NotifyDriverDocumentExpiry::class,
        AssignDriversForScheduledRides::class,
        OfflineUnAvailableDrivers::class,
        AssignDriversForRegularRides::class,
        ClearDemoDatabase::class,
        ClearRequestTable::class,

    ];

    /**
     * Define the application's command schedule.
     *
     * @param  \Illuminate\Console\Scheduling\Schedule  $schedule
     * @return void
     */
    protected function schedule(Schedule $schedule)
    {
        $schedule->command('drivers:totrip')
                 ->everyMinute();
         $schedule->command('assign_drivers:for_regular_rides')
                 ->everyMinute();
        $schedule->command('assign_drivers:for_schedule_rides')
                 ->everyFiveMinutes();
        // $schedule->command('clear:database')
        //          ->daily();
    }

    /**
     * Register the Closure based commands for the application.
     *
     * @return void
     */
    protected function commands()
    {
        require base_path('routes/console.php');
    }
}
